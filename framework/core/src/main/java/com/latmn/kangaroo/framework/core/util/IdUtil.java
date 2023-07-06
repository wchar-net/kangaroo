package com.latmn.kangaroo.framework.core.util;

import com.latmn.kangaroo.framework.core.define.Define;

import java.util.UUID;

public class IdUtil {
    public static final String TRACE_ID = Define.TRACE_ID;

    public static String uuid32() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
