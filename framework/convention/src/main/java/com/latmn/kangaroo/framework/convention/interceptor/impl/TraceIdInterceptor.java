package com.latmn.kangaroo.framework.convention.interceptor.impl;

import com.latmn.kangaroo.framework.convention.interceptor.ConventionInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TraceIdInterceptor implements ConventionInterceptor {
    public static final String TRACE_ID = "requestId";

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
            traceId = UUID.randomUUID().toString().replaceAll("-", "");
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
