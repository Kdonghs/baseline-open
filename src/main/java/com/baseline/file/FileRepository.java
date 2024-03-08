package com.baseline.file;

import com.baseline.querydsl.QBooleanBuilder;
import com.baseline.querydsl.QRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Slf4j
@Repository
public class FileRepository extends QRepository<FileEntity, Integer, QFileEntity> {
    public FileRepository(EntityManager em) {
        super(QFileEntity.fileEntity, em);
    }

    public Long getMemberFileCount(int memberId) {
        return from().where(new QBooleanBuilder()
                .and(table.member.id.eq(memberId))
                .build()
        ).select(table.count())
                .fetchOne();
    }

    public Optional<FileEntity> findByIdAndMemberId(int fileId, int memberId) {
        return Optional.ofNullable(
                from().where(new QBooleanBuilder()
                        .and(table.id.eq(fileId))
                        .and(table.member.id.eq(memberId))
                        .build()
                ).fetchOne()
        );
    }
}
