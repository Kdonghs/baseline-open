package com.baseline.repository;

import com.baseline.entity.QRoleEntity;
import com.baseline.entity.RoleEntity;
import com.baseline.enums.RoleName;
import com.baseline.enums.RoleType;
import com.baseline.querydsl.QRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class RoleRepository extends QRepository<RoleEntity, Integer, QRoleEntity> {
    public RoleRepository(EntityManager em) {
        super(QRoleEntity.roleEntity, em);
    }

    public Optional<RoleEntity> findConferenceAdminRole() {
        return Optional.ofNullable(from()
                .where(table.name.eq(RoleName.CONF_ADMIN))
                .fetchOne()
        );
    }

    // 프로그램 위원장, 프로그램 위원의 role을 조회
    public List<RoleEntity> findConferenceJudgeRoles() {
        return from()
                .where(table.type.eq(RoleType.PROGRAM))
                .fetch();
    }
}
