package com.latmn.kangaroo.framework.convention.exception;

import com.latmn.kangaroo.framework.core.exception.FrameworkException;

public class ConventionException extends FrameworkException {
    public ConventionException(String message) {
        super(message);
    }

    public ConventionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConventionException(Throwable cause) {
        super(cause);
    }

    public ConventionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ConventionException() {
    }
}
