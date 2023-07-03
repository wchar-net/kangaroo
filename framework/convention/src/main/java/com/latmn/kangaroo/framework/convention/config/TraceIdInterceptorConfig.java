package com.latmn.kangaroo.framework.convention.config;

import com.latmn.kangaroo.framework.convention.interceptor.ConventionInterceptor;
import com.latmn.kangaroo.framework.convention.interceptor.impl.TraceIdInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TraceIdInterceptorConfig {
    @Bean
    public ConventionInterceptor traceIdInterceptor() {
        return new TraceIdInterceptor();
    }
}
