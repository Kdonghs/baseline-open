package com.baseline.querydsl;

import com.querydsl.core.NonUniqueResultException;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAUpdateClause;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public abstract class QRepository<T, I, Q extends EntityPathBase<T>> extends SimpleJpaRepository<T, I> {
    protected Q table;
    protected EntityManager entityManager;
    protected Querydsl queryDsl;

    protected QRepository(Q table, EntityManager entityManager) {
        super((Class<T>) table.getType(), entityManager);
        this.table = table;
        this.entityManager = entityManager;
        this.queryDsl = new Querydsl(entityManager, new PathBuilderFactory().create(table.getType()));
    }



    /**
     * select([S]) 표현을 직접 지정하여 임의의 Entity([F])를 기반으로 하는 쿼리를 생성한다.
     *
     * 예)
     * ```
     * val maxId = select(QSomeTable.someTable.amount.max(), QSomeTable.someTable).fetchOne() ?: 0L
     * ```
     * [fromEntityPath]가 null 이면 [table]로 대신한다.
     * ```
     * val maxId = select(table.amount.max()).fetchOne() ?: 0L
     * ```
     *
     * @param selectExpression select 절에 사용할 [Expression] ex) table.amount.max()
     * @param fromEntityPath from 절에 사용할 테이블의 [EntityPathBase]. 보통은 QClass 이고 null 이라면 [table]로 대신한다.
     */
    protected <S, F> JPQLQuery<S> select(Expression<S> selectExpression, EntityPathBase<F> fromEntityPath) {
        return queryDsl.createQuery(fromEntityPath == null ? table : fromEntityPath).select(selectExpression);
    }

    /**
     * [Q]클래스를 기반으로 하는 쿼리를 생성한다.
     *
     * 만약 부득이하게 ([T]와 맵핑된)대상 테이블이 아닌 테이블을 시작으로 쿼리를 구성해야 한다면 [select]를 이용하거나
     * [querydsl] 변수를 이용하여 [Querydsl.createQuery]를 직접 호출하면 된다.
     *
     * 예)
     * ```
     *  from({ // 여기서 it 은 JPQLQuery 객체이다.
     *          it.leftJoin(table.relatedOther, QRelatedOther.relatedOther).fetchJoin()
     *      })
     *      .where( ... )
     *      .fetch()
     * ```
     *
     * @param queryComposer [JPQLQuery]을 파라미터로 하는 lambda 를 넘기면 호출해준다. 주로 join 등을 처리하려고 할때 쓰임
     */
    public JPQLQuery<T> from(Consumer<JPQLQuery<T>> queryComposer) {
        JPQLQuery<T> query = this.select(table, table);
        if (queryComposer != null) {
            queryComposer.accept(query);
        }

        return query;
    }

    public JPQLQuery<T> from() {
        return this.select(table, table);
    }

    public QBooleanBuilder builder() {
        return new QBooleanBuilder();
    }

    /**
     * where 절을 구성하기위한 [BooleanBuilder]. 이 메소드를 사용하지 않고 그냥 [BooleanBuilder] 를 생성하여 써도 무방하다.
     *
     * 예)
     * ```
     *  val list = findAll(
     *      builder(table.board.eq("notice"))
     *          .andIf(!category.isNullOrEmpty(), table.category.eq(category))
     *          .andIf(!keyword.isNullOrEmpty(), table.title.like("%$keyword%"))
     *  , table.id.desc())
     * ```
     * 만약 가변적인 조건을 구성하는 경우가 아니라면 그냥 [JPQLQuery.where]를 이용하는게 좋다.
     * ```
     *  val list = from().where(
     *      table.type.eq("..."),
     *      table.status.eq("...")
     *  ).orderBy(table.id.desc()).fetch()
     * ```
     * @param initial 초기조건. null 을 기본값으로 하기 때문에 이 파라미터 없이 호출해도 된다.
     * @see [andIf]
     */

    public QBooleanBuilder builder(BooleanExpression initial) {
        if (initial == null) return new QBooleanBuilder();

        else return new QBooleanBuilder().and(initial);
    }

    public JPADeleteClause delete() {
        return new JPADeleteClause(entityManager, table);
    }

    public JPAUpdateClause update() {
        return new JPAUpdateClause(entityManager, table);
    }

    /**
     * @param queryComposer [JPQLQuery]을 파라미터로 하는 lambda 를 넘기면 호출해준다. 주로 join 등을 처리하려고 할때 쓰임
     */
    public List<T> findAll(Predicate predicate, OrderSpecifier order, Long limit, Consumer<JPQLQuery<T>> queryComposer) {
        JPQLQuery<T> query = from(queryComposer).where(predicate);
        if (order != null) query.orderBy(order);
        if (limit != null && limit > 0) query.limit(limit);
        return query.fetch();
    }

    public List<T> findAll(Predicate predicate, List<OrderSpecifier> orders, Long limit, Consumer<JPQLQuery<T>> queryComposer) {
        JPQLQuery<T> query = from(queryComposer).where(predicate);
        if (orders != null && !orders.isEmpty()) query.orderBy(orders.toArray(OrderSpecifier[]::new));
        if (limit != null && limit > 0) query.limit(limit);
        return query.fetch();
    }

    public Page<T> findAll(Predicate predicate, Pageable pageable) {
        JPQLQuery<T> query = from().where(predicate);
        return PageableExecutionUtils.getPage(
                queryDsl.applyPagination(pageable, query).fetch(),
                pageable,
                query::fetchCount
        );
    }

    public Page<T> findAll(Predicate predicate, Pageable pageable, Consumer<JPQLQuery<T>> queryComposer) {
        JPQLQuery<T> query = from(queryComposer).where(predicate);
        return PageableExecutionUtils.getPage(
                queryDsl.applyPagination(pageable, query).fetch(),
                pageable,
                query::fetchCount
        );
    }

    public Optional<T> findOne(Predicate predicate, Consumer<JPQLQuery<T>> queryConsumer) {
        try {
            return Optional.ofNullable(from(queryConsumer).where(predicate).fetchOne());
        } catch(NonUniqueResultException e) {
            throw new IncorrectResultSizeDataAccessException(e.getMessage(), 1, e);
        }
    }

    public Long count(Predicate predicate, Consumer<JPQLQuery<T>> queryConsumer) {
        return from(queryConsumer).where(predicate).fetchCount();
    }
}
