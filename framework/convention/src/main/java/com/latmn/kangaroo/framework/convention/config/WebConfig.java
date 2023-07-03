package com.latmn.kangaroo.framework.convention.config;

import com.latmn.kangaroo.framework.convention.interceptor.ConventionInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    private final List<ConventionInterceptor> conventionInterceptions;

    public WebConfig(List<ConventionInterceptor> conventionInterceptions) {
        this.conventionInterceptions = conventionInterceptions;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        for (ConventionInterceptor interception : conventionInterceptions) {
            if(CollectionUtils.isEmpty(interception.exclude())){
                registry.addInterceptor(interception).order(interception.order()).addPathPatterns(interception.path());
            }else {
                registry.addInterceptor(interception).order(interception.order()).addPathPatterns(interception.path()).excludePathPatterns(interception.exclude());
            }
        }
    }
}
