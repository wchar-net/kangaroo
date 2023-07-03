package com.latmn.kangaroo.framework.convention.config;

import com.latmn.kangaroo.framework.convention.core.ConventionCode;
import com.latmn.kangaroo.framework.convention.core.impl.ConventionCodeImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CodeConfig {
    @Bean
    @ConditionalOnMissingBean
    public ConventionCode conventionCode() {
        return new ConventionCodeImpl();
    }
}
