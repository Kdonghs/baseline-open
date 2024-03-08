package com.baseline.common.result;

import lombok.Getter;
import lombok.ToString;

import java.util.Collection;

@Getter
@ToString
public class PageResult<T> extends ListResult<T> {
    private final Integer size;
    private final Integer page;
    private final Integer pages;
    private final Boolean hasNext;
    private final Long total;

    public PageResult(
            Collection<T> initList,
            Integer size,
            Integer page,
            Integer pages,
            Boolean hasNext,
            Long total
    ) {
        super(initList);
        this.size = size != null ? size : 0;
        this.page = page != null ? page : 0;
        this.pages = pages != null ? pages : 0;
        this.hasNext = hasNext;
        this.total = total;
    }
}
