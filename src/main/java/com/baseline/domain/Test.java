package com.baseline.domain;

import com.baseline.common.PageParam;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

public class Test {
    @Data
    public static class Simple {
        private Integer id;
        private String name;
        private Integer age;
    }

    @Getter @Setter
    @ToString
    public static class Req extends PageParam {
        @NotEmpty
        private String name;
    }
}
