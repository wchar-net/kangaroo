package com.latmn.kangaroo.framework.core.exception;

public class KangarooException extends RuntimeException {

    private String errCode;
    private String errMessage;

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

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
