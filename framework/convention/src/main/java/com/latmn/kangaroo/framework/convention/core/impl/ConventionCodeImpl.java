package com.latmn.kangaroo.framework.convention.core.impl;

import com.latmn.kangaroo.framework.convention.core.ConventionCode;

public class ConventionCodeImpl implements ConventionCode {
    @Override
    public String successCode() {
        return "1";
    }

    @Override
    public String errorCode() {
        return "0";
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
