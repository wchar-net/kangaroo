package com.latmn.kangaroo.framework.convention.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kangaroo.exception")
public class ExceptionProp {
    private boolean printStackTrace = false;

    public boolean isPrintStackTrace() {
        return printStackTrace;
    }

    public void setPrintStackTrace(boolean printStackTrace) {
        this.printStackTrace = printStackTrace;
    }
}
