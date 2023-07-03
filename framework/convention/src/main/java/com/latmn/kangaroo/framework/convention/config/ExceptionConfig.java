
package com.latmn.kangaroo.framework.convention.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.latmn.kangaroo.framework.convention.core.ConventionCode;
import com.latmn.kangaroo.framework.convention.interceptor.impl.TraceIdInterceptor;
import com.latmn.kangaroo.framework.core.result.FieldResult;
import com.latmn.kangaroo.framework.core.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@RestControllerAdvice
public class ExceptionConfig {

    private final Logger logger = LoggerFactory.getLogger(ExceptionConfig.class);


    @Autowired
    private ConventionCode conventionCode;

    @Autowired
    private ObjectMapper objectMapper;

    @ExceptionHandler(ConstraintViolationException.class)
    public Result handleConstraintViolationException(ConstraintViolationException ex, WebRequest webRequest) {
        ex.printStackTrace();
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        List<FieldResult> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : constraintViolations) {
            String name = "";
            Path pathObj = violation.getPropertyPath();
            if (null != pathObj) {
                String pathStr = pathObj.toString();
                int index = violation.getPropertyPath().toString().lastIndexOf('.');
                if (index > -1) {
                    index = index + 1;
                    if (index <= pathStr.length()) {
                        name = pathStr.substring(index);
                    }

                }
            }
            errors.add(new FieldResult(name, violation.getMessage(), violation.getInvalidValue()));
        }
        Result result = new Result();
        result.setCode(conventionCode.paramValidFailCode());
        result.setMessage(conventionCode.paramValidFailMessage());
        result.setData(errors);
        handlerResult(result, webRequest);
        return result;
    }


    @ExceptionHandler(BindException.class)
    public Result handleBindException(BindException ex, WebRequest webRequest) throws BindException {
        ex.printStackTrace();
        List<FieldResult> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(new FieldResult(error.getField(), error.getDefaultMessage(), error.getRejectedValue()));
        }
        Result result = new Result();
        result.setCode(conventionCode.paramValidFailCode());
        result.setMessage(conventionCode.paramValidFailMessage());
        result.setData(errors);
        handlerResult(result, webRequest);
        return result;
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest webRequest) throws MethodArgumentNotValidException {
        ex.printStackTrace();
        List<FieldResult> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(new FieldResult(error.getField(), error.getDefaultMessage(), error.getRejectedValue()));
        }
        Result result = new Result();
        result.setCode(conventionCode.paramValidFailCode());
        result.setMessage(conventionCode.paramValidFailMessage());
        result.setData(errors);
        handlerResult(result, webRequest);
        return result;
    }


    @ExceptionHandler({Exception.class})
    public Result handlerException(Exception e, WebRequest webRequest) {
        e.printStackTrace();
        Result result = new Result();
        result.setCode(conventionCode.errorCode());
        result.setMessage(e.getMessage());
        result.setData(e.getMessage());
        handlerResult(result, webRequest);
        return result;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public Result handleNoHandlerFound(NoHandlerFoundException e, WebRequest request) {
        Result result = new Result<>();
        result.setCode(conventionCode.errorCode());
        result.setMessage(e.getMessage());
        result.setMessage(e.getMessage());
        handlerResult(result, request);
        return result;
    }

    private void handlerResult(Result result, WebRequest webRequest) {
        String requestId = webRequest.getHeader(TraceIdInterceptor.TRACE_ID);
        if (!StringUtils.hasText(requestId)) {
            requestId = UUID.randomUUID().toString().replaceAll("-", "");
            MDC.clear();
            MDC.put(TraceIdInterceptor.TRACE_ID, requestId);
            NativeWebRequest nativeWebRequest = (NativeWebRequest) webRequest;
            HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
            HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
            response.addHeader(TraceIdInterceptor.TRACE_ID, requestId);
            result.setRequestId(requestId);
            result.setRequestPath(request.getRequestURI());
        }
        logger.error("error: {}", result.getMessage());
    }
}