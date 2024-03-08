package com.baseline.service;

import com.baseline.domain.Test;
import com.baseline.mapper.TestMapper;
import com.baseline.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.validation.Valid;


@RequiredArgsConstructor
@Service
public class TestService {
    private final TestRepository testRepository;
    private final TestMapper testMapper;

    public Test.Simple getTestById(Integer id) throws Exception {
        return testMapper.toTestDto(testRepository.findById(id)
                .orElseThrow(() -> new Exception(""))
        );
    }

    public Page<Test.Simple> getTest(@Valid Test.Req request) {
        return testRepository.findPage(request.getName(), request.toPageable()).map(testMapper::toTestDto);
    }
}
