package com.latmn.kangaroo.framework.core.exception;

import com.latmn.kangaroo.framework.core.define.Define;

public class UserException extends PlatformException {
    public UserException() {
    }

    public UserException(String message) {
        super(message);
        this.setErrCode(Define.AUTH_ERROR_CODE);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserException(Throwable cause) {
        super(cause);
    }

    public UserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
