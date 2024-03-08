package com.baseline.mapper;

import com.baseline.domain.Test;
import com.baseline.entity.TestEntity;
import org.springframework.stereotype.Component;

@Component
public class TestMapper {
    public Test.Simple toTestDto(TestEntity testEntity) {
        Test.Simple simple = new Test.Simple();
        simple.setId(testEntity.getId());
        simple.setName(testEntity.getName());
        simple.setAge(testEntity.getAge());
        return simple;
    }
}
