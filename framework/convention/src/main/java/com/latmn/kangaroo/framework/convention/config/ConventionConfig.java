package com.latmn.kangaroo.framework.convention.config;

import com.latmn.kangaroo.framework.convention.prop.ExceptionProp;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Configuration
@EnableConfigurationProperties(ExceptionProp.class)
@Import({CodeConfig.class, ReturnValueHandlerConfig.class, WebConfig.class, TraceIdInterceptorConfig.class, ExceptionConfig.class})
public class ConventionConfig {

    @Bean
    public LocalValidatorFactoryBean validatorFactory() {
        return new LocalValidatorFactoryBean();
    }
}
