package com.latmn.kangaroo.framework.convention.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.latmn.kangaroo.framework.convention.RT;
import com.latmn.kangaroo.framework.convention.RTPage;
import com.latmn.kangaroo.framework.convention.core.ConventionCode;
import com.latmn.kangaroo.framework.convention.interceptor.impl.TraceIdInterceptor;
import com.latmn.kangaroo.framework.core.result.PageResult;
import com.latmn.kangaroo.framework.core.result.Result;
import com.latmn.kangaroo.framework.convention.util.ViewUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

public class ReturnValueHandler implements HandlerMethodReturnValueHandler {
    protected final HandlerMethodReturnValueHandler handlerMethodReturnValueHandler;
    private final ObjectMapper objectMapper;
    private final ConventionCode conventionCode;

    public ReturnValueHandler(ConventionCode conventionCode, ObjectMapper objectMapper, HandlerMethodReturnValueHandler handlerMethodReturnValueHandler) {
        this.objectMapper = objectMapper;
        this.handlerMethodReturnValueHandler = handlerMethodReturnValueHandler;
        this.conventionCode = conventionCode;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return (AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ResponseBody.class) ||
                returnType.hasMethodAnnotation(ResponseBody.class));
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {

        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request.getRequestURI().contains("/v3/api-docs")) {
            handlerMethodReturnValueHandler.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
            return;
        }
        mavContainer.setRequestHandled(Boolean.TRUE);
        if (returnValue instanceof RT) {
            RT rt = (RT) returnValue;
            Result original = rt.original();
            String code = original.getCode();
            String message = original.getMessage();
            original.setRequestPath(request.getRequestURI());
            original.setRequestId(response.getHeader(TraceIdInterceptor.TRACE_ID));
            if (StringUtils.isEmpty(code))
                original.setCode(conventionCode.successCode());
            if (StringUtils.isEmpty(message))
                original.setMessage(conventionCode.successMessage());
            ViewUtil.writeJson(objectMapper, original, response);
        } else if (returnValue instanceof RTPage) {
            RTPage rtPage = (RTPage) returnValue;
            PageResult original = rtPage.original();
            String code = original.getCode();
            String message = original.getMessage();
            original.setRequestPath(request.getRequestURI());
            original.setRequestId(response.getHeader(TraceIdInterceptor.TRACE_ID));
            if (StringUtils.isEmpty(code))
                original.setCode(conventionCode.successCode());
            if (StringUtils.isEmpty(message))
                original.setMessage(conventionCode.successMessage());
            ViewUtil.writeJson(objectMapper, original, response);
        } else {
            if (returnValue instanceof Result) {
                Result original = (Result) returnValue;
                if (StringUtils.isEmpty(original.getCode())) {
                    original.setCode(conventionCode.successCode());
                }
                if (StringUtils.isEmpty(original.getMessage())) {
                    original.setMessage(conventionCode.successMessage());
                }
                original.setRequestPath(request.getRequestURI());
                original.setRequestId(response.getHeader(TraceIdInterceptor.TRACE_ID));
                ViewUtil.writeJson(objectMapper, original, response);
            } else if (returnValue instanceof PageResult) {
                PageResult original = (PageResult) returnValue;
                if (StringUtils.isEmpty(original.getCode())) {
                    original.setCode(conventionCode.successCode());
                }
                if (StringUtils.isEmpty(original.getMessage())) {
                    original.setMessage(conventionCode.successMessage());
                }
                original.setRequestPath(request.getRequestURI());
                original.setRequestId(response.getHeader(TraceIdInterceptor.TRACE_ID));
                ViewUtil.writeJson(objectMapper, original, response);
            } else {
                Result<Object> result = new Result<>();
                result.setCode(conventionCode.successCode());
                result.setMessage(conventionCode.successMessage());
                result.setData(returnValue);
                result.setRequestPath(request.getRequestURI());
                result.setRequestId(response.getHeader(TraceIdInterceptor.TRACE_ID));
                ViewUtil.writeJson(objectMapper, result, response);
            }
        }
    }
}
