package com.latmn.kangaroo.framework.convention.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({CodeConfig.class, ReturnValueHandlerConfig.class, WebConfig.class, TraceIdInterceptorConfig.class, ExceptionConfig.class})
public class ConventionConfig {
}
