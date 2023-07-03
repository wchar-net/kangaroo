package com.latmn.kangaroo.framework.core.result;

import java.util.List;


public class MethodArgumentResult<T> {
    private List<FieldResult> paramErrors;

    public MethodArgumentResult(List<FieldResult> paramErrors) {
        this.paramErrors = paramErrors;
    }

    public List<FieldResult> getParamErrors() {
        return paramErrors;
    }

    public void setParamErrors(List<FieldResult> paramErrors) {
        this.paramErrors = paramErrors;
    }

    private MethodArgumentResult() {
    }
}
