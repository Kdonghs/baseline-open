package com.baseline.repository;

import com.baseline.entity.TestEntity;
import com.baseline.entity.QTestEntity;
import com.baseline.querydsl.QRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;

@Repository
public class TestRepository extends QRepository<TestEntity, Integer, QTestEntity> {
    public TestRepository(EntityManager em) {
        super(QTestEntity.testEntity, em);
    }

    public Page<TestEntity> findPage(String name, Pageable pageable) {
        return findAll(builder()
                .andIf(!ObjectUtils.isEmpty(name), () -> table.name.eq(name))
                .build()
        , pageable);
    }
}
