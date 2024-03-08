package com.baseline.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Getter @Setter
public class PageParam {
    private Integer page = 0;
    private Integer size = 10;
    private String sort = "";

    public PageRequest toPageable() {
        return PageRequest.of(
                page < 1 ? 0 : page,
                size < 1 ? Integer.MAX_VALUE : size,
                // TODO sort 구현 필요
                sort.isEmpty() ? Sort.unsorted() : Sort.unsorted()
        );
    }
}
