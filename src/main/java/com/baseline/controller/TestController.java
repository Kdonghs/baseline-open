package com.baseline.controller;

import com.baseline.common.result.PageResult;
import com.baseline.common.result.SingleResult;
import com.baseline.domain.Test;
import com.baseline.mapper.PageMapper;
import com.baseline.service.TestService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TestController {
    private final TestService testService;

    @GetMapping("/")
    public SingleResult<String> test() {
        return new SingleResult<>("hello");
    }

    @ApiOperation("test")
    @GetMapping("/test/{id}")
    public SingleResult<Test.Simple> getTestById(@PathVariable Integer id) throws Exception {
        return new SingleResult<>(testService.getTestById(id));
    }

    @ApiOperation("test list")
    @GetMapping("/test")
    public PageResult<Test.Simple> getTest(Test.Req request) {
        return PageMapper.toPageResult(testService.getTest(request));
    }
}
