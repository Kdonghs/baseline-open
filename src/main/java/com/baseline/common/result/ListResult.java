package com.baseline.common.result;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@ToString
@Getter
public class ListResult <T> extends BaseResult {
    private final List<T> resultData = new ArrayList<>();

    public ListResult(Collection<T> initList) {
        if (initList != null) resultData.addAll(initList);
    }

    public void addAll(Collection<T> others) {
        resultData.addAll(others);
    }

    public void add(T element) {
        resultData.add(element);
    }


}
