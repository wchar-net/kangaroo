package com.latmn.kangaroo.framework.convention.core.impl;

import com.latmn.kangaroo.framework.convention.core.ConventionCode;
import com.latmn.kangaroo.framework.core.define.Define;

public class ConventionCodeImpl implements ConventionCode {
    @Override
    public String successCode() {
        return Define.SUCCESS_CODE;
    }

    @Override
    public String errorCode() {
        return Define.ERROR_CODE;
    }

    @Override
    public String successMessage() {
        return "success";
    }

    @Override
    public String errorMessage() {
        return "error";
    }

    @Override
    public String paramValidFailCode() {
        return "P_500";
    }

    @Override
    public String paramValidFailMessage() {
        return "Param Valid Fail";
    }
}
