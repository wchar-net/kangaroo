
package com.latmn.kangaroo.framework.convention.config;

import com.latmn.kangaroo.framework.convention.core.ConventionCode;
import com.latmn.kangaroo.framework.convention.interceptor.impl.TraceIdInterceptor;
import com.latmn.kangaroo.framework.convention.prop.ExceptionProp;
import com.latmn.kangaroo.framework.core.exception.KangarooException;
import com.latmn.kangaroo.framework.core.result.FieldResult;
import com.latmn.kangaroo.framework.core.result.Result;
import com.latmn.kangaroo.framework.core.util.ExceptionUtil;
import com.latmn.kangaroo.framework.core.util.IdUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
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


@RestControllerAdvice
public class ExceptionConfig {

    private final Logger logger = LoggerFactory.getLogger(ExceptionConfig.class);


    private final ConventionCode conventionCode;
    private final ExceptionProp exceptionProp;

    public ExceptionConfig(ConventionCode conventionCode, ExceptionProp exceptionProp) {
        this.conventionCode = conventionCode;
        this.exceptionProp = exceptionProp;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Result handleConstraintViolationException(ConstraintViolationException ex, WebRequest webRequest) {
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
        Result<Object> result = Result.builder().code(conventionCode.paramValidFailCode()).errData(errors).errMessage(conventionCode.paramValidFailMessage()).build();
        handlerResult(result, webRequest);
        logger.error(ex.getMessage(), ex);
        return result;
    }


    @ExceptionHandler(BindException.class)
    public Result handleBindException(BindException ex, WebRequest webRequest) {
        List<FieldResult> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(new FieldResult(error.getField(), error.getDefaultMessage(), error.getRejectedValue()));
        }

        Result<Object> result = Result.builder().code(conventionCode.paramValidFailCode()).errData(errors).errMessage(conventionCode.paramValidFailMessage()).build();
        handlerResult(result, webRequest);
        logger.error(ex.getMessage(), ex);
        return result;
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest webRequest) {
        List<FieldResult> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(new FieldResult(error.getField(), error.getDefaultMessage(), error.getRejectedValue()));
        }
        Result<Object> result = Result.builder().code(conventionCode.paramValidFailCode()).errData(errors).errMessage(conventionCode.paramValidFailMessage()).build();
        handlerResult(result, webRequest);
        logger.error(ex.getMessage(), ex);
        return result;
    }


    @ExceptionHandler({KangarooException.class})
    public Result handlerException(KangarooException e, WebRequest webRequest) {
        Result result = new Result();
        result.setCode(StringUtils.hasText(e.getErrCode()) ? e.getErrCode() : conventionCode.errorCode());
        result.setMessage(StringUtils.hasText(e.getErrMessage()) ? e.getErrMessage() : conventionCode.errorMessage());
        if (!StringUtils.hasText(e.getMessage())) {
            if(exceptionProp.isPrintStackTrace()){
                result.setErrMessage(ExceptionUtil.getStackTrace(e));
            }
        } else {
            result.setErrMessage(e.getMessage());
            result.setErrData(e.getMessage());
        }
        handlerResult(result, webRequest);
        logger.error(e.getMessage(), e);
        return result;
    }

    @ExceptionHandler({Exception.class})
    public Result handlerException(Exception e, WebRequest webRequest) {
        Result result = new Result();
        result.setCode(conventionCode.errorCode());
        if (!StringUtils.hasText(e.getMessage())) {
            if(exceptionProp.isPrintStackTrace()){
                result.setErrMessage(ExceptionUtil.getStackTrace(e));
            }
        } else {
            result.setErrMessage(e.getMessage());
            result.setErrData(e.getMessage());
        }
        handlerResult(result, webRequest);
        logger.error(e.getMessage(), e);
        return result;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public Result handleNoHandlerFound(NoHandlerFoundException e, WebRequest request) {
        logger.error(e.getMessage(), e);
        Result result = new Result<>();
        result.setCode(conventionCode.errorCode());
        result.setErrMessage(e.getMessage());
        result.setErrData(e.getMessage());
        handlerResult(result, request);
        logger.error(e.getMessage(), e);
        return result;
    }

    private void handlerResult(Result result, WebRequest webRequest) {
        String requestId = webRequest.getHeader(TraceIdInterceptor.TRACE_ID);
        NativeWebRequest nativeWebRequest = (NativeWebRequest) webRequest;
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
        if (!StringUtils.hasText(requestId)) {
            requestId = IdUtil.uuid32();
            MDC.clear();
            MDC.put(TraceIdInterceptor.TRACE_ID, requestId);
            response.addHeader(TraceIdInterceptor.TRACE_ID, requestId);
        }
        result.setRequestId(requestId);
        result.setRequestPath(request.getRequestURI());
    }
}