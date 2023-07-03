package com.latmn.kangaroo.framework.convention.core;

public interface ConventionCode {
    String successCode();

    String errorCode();

    String successMessage();

    String errorMessage();

    String paramValidFailCode();

    String paramValidFailMessage();
}
