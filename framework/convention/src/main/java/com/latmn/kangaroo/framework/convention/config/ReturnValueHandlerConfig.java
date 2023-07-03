package com.latmn.kangaroo.framework.convention.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.latmn.kangaroo.framework.convention.core.ConventionCode;
import com.latmn.kangaroo.framework.convention.handler.ReturnValueHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ReturnValueHandlerConfig implements InitializingBean {


    private final RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    private final ObjectMapper objectMapper;
    private final ConventionCode conventionCode;


    public ReturnValueHandlerConfig(RequestMappingHandlerAdapter requestMappingHandlerAdapter,
                                    ObjectMapper objectMapper, ConventionCode conventionCode) {
        this.requestMappingHandlerAdapter = requestMappingHandlerAdapter;
        this.objectMapper = objectMapper;
        this.conventionCode = conventionCode;
    }


    @Override
    public void afterPropertiesSet() {
        List<HandlerMethodReturnValueHandler> handlerMethodReturnValueHandlerList = requestMappingHandlerAdapter.getReturnValueHandlers();
        if (null != handlerMethodReturnValueHandlerList) {
            List<HandlerMethodReturnValueHandler> handlerMethodReturnValueHandlers = new ArrayList<>(handlerMethodReturnValueHandlerList.size());
            handlerMethodReturnValueHandlerList.forEach(handlerMethodReturnValueHandler -> {
                if (handlerMethodReturnValueHandler instanceof RequestResponseBodyMethodProcessor)
                    handlerMethodReturnValueHandlers.add(new ReturnValueHandler(conventionCode, objectMapper, handlerMethodReturnValueHandler));
                else
                    handlerMethodReturnValueHandlers.add(handlerMethodReturnValueHandler);
            });
            requestMappingHandlerAdapter.setReturnValueHandlers(handlerMethodReturnValueHandlers);
        }
    }
}
