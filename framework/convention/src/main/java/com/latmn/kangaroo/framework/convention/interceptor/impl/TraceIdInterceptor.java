package com.latmn.kangaroo.framework.convention.interceptor.impl;

import com.latmn.kangaroo.framework.convention.interceptor.ConventionInterceptor;
import com.latmn.kangaroo.framework.core.util.IdUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

public class TraceIdInterceptor implements ConventionInterceptor {
    public static final String TRACE_ID = IdUtil.TRACE_ID;

    @Override
    public int order() {
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }

    @Override
    public List<String> path() {
        return Arrays.asList("/**");
    }

    @Override
    public List<String> exclude() {
        return null;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String traceId = getTraceId(request);
        if (!StringUtils.hasText(traceId)) {
            traceId = IdUtil.uuid32();
        }
        MDC.clear();
        MDC.put(TRACE_ID, traceId);
        response.addHeader(TRACE_ID, traceId);
        return true;
    }

    private String getTraceId(HttpServletRequest request) {
        return request.getHeader(TRACE_ID);
    }
}
