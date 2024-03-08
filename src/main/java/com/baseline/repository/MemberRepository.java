package com.baseline.repository;

import com.baseline.entity.MemberEntity;
import com.baseline.entity.QMemberEntity;
import com.baseline.querydsl.QRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public class MemberRepository extends QRepository<MemberEntity, Integer, QMemberEntity> {
    public MemberRepository(EntityManager em) {
        super(QMemberEntity.memberEntity, em);
    }

    /**
     *
     * @param uid oauth2면 resource owner의 uid, 아니면 이메일 비밀번호
     */
    public Optional<MemberEntity> findByUid(String uid) {
        return Optional.ofNullable(from().where(table.uid.eq(uid)).fetchOne());
    }

}
