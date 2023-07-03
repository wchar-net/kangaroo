package com.latmn.kangaroo.framework.convention;

import com.latmn.kangaroo.framework.convention.exception.ConventionException;
import com.latmn.kangaroo.framework.core.result.Result;

import java.io.Serializable;
import java.util.List;
import java.util.function.*;

/**
 * @author Elijah
 * RT
 */
public final class RT<T> implements Serializable {

    private Convention<T> convention = new Convention<>();

    private final static String RT_CREATE_SUPPLIER_NOT_NULL = " RT Create Supplier, Not Null!";
    private final static String RT_JUST_SUPPLIER_NOT_NULL = "RT Just Supplier, Not Null!";
    private final String RT_SWITCH_EMPTY_PARAM_NOT_NULL = "RT SwitchEmpty Data, Not Null!";
    private final String RT_SWITCH_EMPTY_SUPPLIER_NOT_NULL = "RT SwitchEmpty Supplier, Not Null!";
    private final String RT_HANDLER_CONSUMER_NOT_NULL = "RT Handler Consumer, Not Null!";
    private final String RT_HANDLER_SUPPLIER_NOT_NULL = "RT Handler Supplier, Not Null!";
    private final String RT_HANDLER_FUNCTION_NOT_NULL = "RT Handler Function, Not Null!";


    private RT() {
    }

    private String getCode() {
        return this.convention.code;
    }

    private void setCode(String code) {
        this.convention.code = code;
    }

    private String getMessage() {
        return this.convention.message;
    }

    private void setMessage(String message) {
        this.convention.message = message;
    }

    private T getData() {
        return this.convention.data;
    }

    private void setData(T data) {
        this.convention.data = data;
    }

    public RT<T> code(String code) {
        this.convention.code = code;
        return this;
    }

    public RT<T> message(String message) {
        this.convention.message = message;
        return this;
    }


    public Result<T> original() {
        Result<T> result = new Result<>();
        result.setCode(this.convention.code);
        result.setMessage(this.convention.message);
        result.setData(this.convention.data);
        return result;
    }

    private RT(T data) {
        this.convention.data = data;
    }

    private RT(String code, String message, T data) {
        this.convention.code = code;
        this.convention.message = message;
        this.convention.data = data;
    }


    public static <T> RT<T> create(T param) {
        return new RT<>(param);
    }

    public static <T> RT<T> create(Supplier<T> param) {
        if (null == param)
            throw new ConventionException(RT_CREATE_SUPPLIER_NOT_NULL);
        return new RT<>(param.get());
    }

    public static <T> RT<T> create(String code, String message, T data) {
        return new RT<>(code, message, data);
    }

    public static <T> RT<T> just(T param) {
        return new RT<>(param);
    }

    public static <T> RT<T> just(Supplier<T> param) {
        if (null == param)
            throw new ConventionException(RT_JUST_SUPPLIER_NOT_NULL);
        return new RT<>(param.get());
    }

    public RT<T> switchEmpty(T param) {
        if (null == param)
            throw new ConventionException(RT_SWITCH_EMPTY_PARAM_NOT_NULL);
        if (null == this.convention.data)
            this.convention.data = param;
        return this;
    }

    public RT<T> switchEmpty(Supplier<T> param) {
        if (null == param)
            throw new ConventionException(RT_SWITCH_EMPTY_SUPPLIER_NOT_NULL);
        T data = param.get();
        if (null == data)
            throw new ConventionException(RT_SWITCH_EMPTY_PARAM_NOT_NULL);
        if (null == this.convention.data)
            this.convention.data = param.get();
        return this;
    }

    public RT<T> switchEmpty(RT<T> param) {
        if (null == param)
            throw new ConventionException(RT_SWITCH_EMPTY_PARAM_NOT_NULL);
        Convention<T> convention = clone(param);
        if (null == convention.data)
            throw new ConventionException(RT_SWITCH_EMPTY_PARAM_NOT_NULL);
        if (null == this.convention.data)
            this.convention = convention;
        return this;
    }

    public RT<T> handler(Consumer<T> param) {
        if (null == param)
            throw new ConventionException(RT_HANDLER_CONSUMER_NOT_NULL);
        else
            param.accept(this.convention.data);
        return this;
    }

    public RT<T> handler(Supplier<T> param) {
        if (null == param)
            throw new ConventionException(RT_HANDLER_SUPPLIER_NOT_NULL);
        else
            this.convention.data = param.get();
        return this;
    }

    public RT<T> handler(Function<T, T> param) {
        if (null == param)
            throw new ConventionException(RT_HANDLER_FUNCTION_NOT_NULL);
        else {
            this.convention.data = param.apply(this.convention.data);
        }
        return this;
    }

    private final String RT_CHANGE_FUNCTION_NOT_NULL = "RT Change Function, Not Null!";

    public <R> RT<R> map(Function<T, R> param) {
        if (null == param)
            throw new ConventionException(RT_CHANGE_FUNCTION_NOT_NULL);
        else {
            R newR = param.apply(this.convention.data);
            return create(this.convention.code, this.convention.message, newR);
        }
    }

    private final String RT_FLATMAP_FUNCTION_NOT_NULL = "RT FlatMap Function, Not Null!";

    public <R> RT<R> flatMap(Function<T, RT<R>> param) {
        if (null == param)
            throw new ConventionException(RT_FLATMAP_FUNCTION_NOT_NULL);
        else {
            RT<R> newR = param.apply(this.convention.data);
            newR.convention.code = this.convention.code;
            newR.convention.message = this.convention.code;
            return newR;
        }
    }

    public <R> RT<R> change(Function<T, RT<R>> param) {
        if (null == param)
            throw new ConventionException(RT_CHANGE_FUNCTION_NOT_NULL);
        else {
            RT<R> newR = param.apply(this.convention.data);
            newR.convention.code = this.convention.code;
            newR.convention.message = this.convention.code;
            return newR;
        }
    }


    private final String RT_PEEK_FUNCTION_NOT_NULL = "RT Peek Consumer, Not Null!";

    public RT<T> peek(Consumer<T> param) {
        if (null == param)
            throw new ConventionException(RT_PEEK_FUNCTION_NOT_NULL);
        else
            param.accept(this.convention.data);
        return this;
    }

    private final String RT_VALID_VALUE_NOT_NULL = "RT valid fail!, Data, Not Null!";

    public RT<T> valid() {
        if (null == this.convention.data)
            throw new ConventionException(RT_VALID_VALUE_NOT_NULL);
        return this;
    }

    private final String RT_VALID_SUPPLIER_NOT_NULL = "RT valid fail!, Supplier, Not Null!";

    public RT<T> valid(Supplier<? extends RuntimeException> supplier) {
        if (null == supplier)
            throw new ConventionException(RT_VALID_SUPPLIER_NOT_NULL);
        if (null == this.convention.data)
            throw supplier.get();
        return this;
    }

    private final String RT_VALID_EXCEPTION_NOT_NULL = "RT valid fail!, E, Not Null!";

    public RT<T> valid(RuntimeException e) {
        if (null == e)
            throw new ConventionException(RT_VALID_EXCEPTION_NOT_NULL);
        if (null == this.convention.data)
            throw e;
        return this;
    }


    private final String RT_VALID_CONSUMER_NOT_NULL = "RT Valid Function, Consumer, Not Null!";

    public RT<T> valid(Consumer<T> param) {
        if (null == param)
            throw new ConventionException(RT_VALID_CONSUMER_NOT_NULL);

        param.accept(this.convention.data);
        return this;
    }

    private final String RT_VALID_PARAM_NOT_NULL = "RT Valid Function, Function, Not Null!";
    private final String RT_VALID_E_NOT_NULL = "RT Valid Function, e, Not Null!";

    public <R> RT<T> valid(Function<T, R> param, ConventionException e) {
        if (null == param)
            throw new ConventionException(RT_VALID_PARAM_NOT_NULL);
        if (null == e)
            throw new ConventionException(RT_VALID_E_NOT_NULL);
        if (null == param.apply(this.convention.data))
            throw e;
        return this;
    }

    public <R> RT<T> valid(Function<T, R> param, Consumer<T> e) {
        if (null == param)
            throw new ConventionException(RT_VALID_PARAM_NOT_NULL);
        if (null == e)
            throw new ConventionException(RT_VALID_CONSUMER_NOT_NULL);
        e.accept(this.convention.data);
        return this;
    }

    public <R> RT<T> valid(Function<T, R> param, Supplier<? extends ConventionException> supplier) {
        if (null == param)
            throw new ConventionException(RT_VALID_PARAM_NOT_NULL);
        if (null == supplier)
            throw new ConventionException(RT_VALID_SUPPLIER_NOT_NULL);
        RuntimeException e = supplier.get();
        if (null == e)
            throw new ConventionException(RT_VALID_E_NOT_NULL);
        if (null == param.apply(this.convention.data))
            throw e;
        return this;
    }

    private final String RT_NEVER_CONSUMER_NOT_NULL = "RT Never Consumer, Not Null!";

    public void never(Consumer<T> consumer) {
        if (null == consumer)
            throw new ConventionException(RT_NEVER_CONSUMER_NOT_NULL);
        consumer.accept(this.convention.data);
    }

    private final String RT_PAGE_TRI_FUNCTION_NOT_NULL = "RT Page TriFunction, Not Null!";
    private final String RT_PAGE_TRI_FUNCTION_LONG_NOT_NULL = "RT Page TriFunction -> Long, Not Null!";
    private final String RT_PAGE_TRI_FUNCTION_LIST_NOT_NULL = "RT Page TriFunction -> List, Not Null!";

    public <R> RTPage<R> page(BiConsumer<T, PageWrapper<R>> param) {
        if (null == param)
            throw new ConventionException(RT_PAGE_TRI_FUNCTION_NOT_NULL);

        PageWrapper<R> pw = new PageWrapper<>();
        param.accept(this.convention.data, pw);

        if (null == pw.pageTotal)
            throw new ConventionException(RT_PAGE_TRI_FUNCTION_LONG_NOT_NULL);
        if (null == pw.pageData)
            throw new ConventionException(RT_PAGE_TRI_FUNCTION_LIST_NOT_NULL);

        return RTPage.create(this.convention.code, this.convention.message, pw.pageTotal, pw.pageData);
    }


    public T source() {
        return this.convention.data;
    }

    private Convention<T> clone(RT<T> param) {
        T data = param.convention.data;
        String code = param.convention.code;
        String message = param.convention.message;
        return new Convention<>(code, message, data);
    }

    public static class PageWrapper<R> implements Serializable {
        public PageWrapper() {
        }

        private Long pageTotal;
        private List<R> pageData;

        public void setPageTotal(Long pageTotal) {
            this.pageTotal = pageTotal;
        }

        public void setPageData(List<R> pageData) {
            this.pageData = pageData;
        }

        @Override
        public String toString() {
            return "PageWrapper{" +
                    "pageTotal=" + pageTotal +
                    ", pageData=" + pageData +
                    '}';
        }

        public PageWrapper(Long pageTotal, List<R> pageData) {
            this.pageTotal = pageTotal;
            this.pageData = pageData;
        }
    }

    private class Convention<T> implements Serializable {
        protected String code;
        protected String message;
        protected T data;

        private Convention(String code, String message, T data) {
            this.code = code;
            this.message = message;
            this.data = data;
        }

        public Convention() {

        }
    }

}
