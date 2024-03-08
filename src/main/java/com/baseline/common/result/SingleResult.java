package com.baseline.common.result;

import lombok.Getter;

@Getter
public class SingleResult<T> extends BaseResult {
    private final T resultData;

    public SingleResult(T value) {
        this.resultData = value;
    }
}
