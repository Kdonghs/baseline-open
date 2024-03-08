package com.baseline.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import java.util.function.Supplier;


public class QBooleanBuilder {
    private BooleanBuilder booleanBuilder;

    public QBooleanBuilder() {
        this.booleanBuilder = new BooleanBuilder();
    }

    public QBooleanBuilder(Predicate initial) {
        this.booleanBuilder = new BooleanBuilder(initial);
    }

    public QBooleanBuilder andIf(boolean condition, Predicate predicate) {
        if (condition) {
            this.booleanBuilder.and(predicate);
        }
        return this;
    }

    public QBooleanBuilder and(Predicate predicate) {
        this.booleanBuilder.and(predicate);
        return this;
    }

    /**
     * [Predicate] 를 직접 입력받는 [andIf] 를 이용할 때는 항상 predicate 가 생성(평가)될 수 밖에 없으므로
     * 이 비용을 무시하기 힘든 경우라면 lambda 를 입력받아서 [condition] 에 따라 predicate 생성이 불필요한 상황을 지원한다.
     * ```
     *      .andIf(!name.isNullOrEmpty(), table.name.eq(name)) // 이 경우 name 이 null 이라면 eq() 에서 NPE 가 발생한다.
     *      .andIf(!name.isNullOrEmpty(), { table.name.eq(name) }) // lambda 로 분리하여 NPE 를 회피한다.
     * ```
     *
     * @param condition 이 조건이 true 일 때 만 [predicateLambda] 를 실행하여 [BooleanBuilder] 에 추가된다.
     * @param predicateSupplier [Predicate]를 return 하는 lambda function
     */
    public QBooleanBuilder andIf(boolean condition, Supplier<Predicate> predicateSupplier) {
        if (condition) this.booleanBuilder = this.booleanBuilder.and(predicateSupplier.get());
        return this;
    }

    public BooleanBuilder build() {
        return this.booleanBuilder;
    }
}
