package com.latmn.kangaroo.framework.core.exception;

public class KangarooException extends RuntimeException{
    public KangarooException() {
    }

    public KangarooException(String message) {
        super(message);
    }

    public KangarooException(String message, Throwable cause) {
        super(message, cause);
    }

    public KangarooException(Throwable cause) {
        super(cause);
    }

    public KangarooException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
