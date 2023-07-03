package com.latmn.kangaroo.framework.convention;

import com.latmn.kangaroo.framework.convention.exception.ConventionException;
import com.latmn.kangaroo.framework.core.result.PageResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Ellijah
 * RTPage
 */
public final class RTPage<T> implements Serializable {
    private Convention<T> convention = new Convention();

    private RTPage() {
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

    private List<T> getData() {
        return this.convention.data;
    }

    private void setData(List<T> data) {
        this.convention.data = data;
    }

    private Long getTotal() {
        return this.convention.total;
    }

    private void setTotal(Long total) {
        this.convention.total = total;
    }

    private static final String RT_CREATE_TOTAL_NOT_NULL = "RTPage Create Function, Total, Not Null!";
    private static final String RT_CREATE_DATA_NOT_NULL = "RTPage Create Function, Data, Not Null!";

    private RTPage(Long total, List<T> data) {
        this.convention.total = total;
        this.convention.data = data;
    }

    private RTPage(String code, String message, Long total, List<T> data) {
        this.convention.code = code;
        this.convention.message = message;
        this.convention.total = total;
        this.convention.data = data;
    }

    public static <E> RTPage<E> create(Long total, List<E> data) {
        if (null == total)
            throw new ConventionException(RT_CREATE_TOTAL_NOT_NULL);
        if (null == data)
            throw new ConventionException(RT_CREATE_DATA_NOT_NULL);
        return new RTPage<>(total, data);
    }

    public static <E> RTPage<E> create(String code, String message, Long total, List<E> data) {
        if (null == total)
            throw new ConventionException(RT_CREATE_TOTAL_NOT_NULL);
        if (null == data)
            throw new ConventionException(RT_CREATE_DATA_NOT_NULL);
        return new RTPage<>(code, message, total, data);
    }

    private final String RT_PEEK_CONSUMER_NOT_NULL = "RTPage Peek Consumer, Not Null!";

    public RTPage<T> peek(Consumer<List<T>> param) {
        if (null == param)
            throw new ConventionException(RT_PEEK_CONSUMER_NOT_NULL);
        else
            param.accept(this.convention.data);
        return this;
    }

    private final String RT_MAP_FUNCTION_NOT_NULL = "RTPage Map Function, Not Null!";

    public <R> RTPage<R> map(Function<T, R> param) {
        if (null == param)
            throw new ConventionException(RT_MAP_FUNCTION_NOT_NULL);
        else {
            List<R> list = new ArrayList<>();
            for (T element : this.convention.data) {
                list.add(param.apply(element));
            }
            return new RTPage<>(this.convention.code, this.convention.message, this.convention.total, list);
        }
    }

    public RT<RT.PageWrapper> toRT() {
        RT.PageWrapper pw = new RT.PageWrapper();
        pw.setPageData(this.convention.data);
        pw.setPageTotal(this.convention.total);
        return RT.create(this.convention.code, this.convention.message, pw);
    }


    private final String RT_FLATMAP_FUNCTION_NOT_NULL = "RTPage FlatMap Function, Not Null!";

    public <R> RTPage<R> flatMap(Function<RTPage<T>, RTPage<R>> param) {
        if (null == param)
            throw new ConventionException(RT_FLATMAP_FUNCTION_NOT_NULL);
        return param.apply(this);
    }

    public RTPage<T> code(String code) {
        this.convention.code = code;
        return this;
    }

    public RTPage<T> message(String message) {
        this.convention.message = message;
        return this;
    }

    public PageResult<T> original() {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setCode(this.convention.code);
        pageResult.setMessage(this.convention.message);
        pageResult.setData(this.convention.data);
        return pageResult;
    }

    public List<T> source() {
        return this.convention.data;
    }

    private final String RT_SOURCE_FUNCTION_NOT_NULL = "RTPage source Function, Not Null!";

    public <R> R source(BiFunction<List<T>, Long, R> function) {
        if (null == function)
            throw new ConventionException(RT_SOURCE_FUNCTION_NOT_NULL);
        return function.apply(this.convention.data, this.convention.total);
    }


    public Long total() {
        return this.convention.total;
    }

    private class Convention<T> implements Serializable {
        protected String code;
        protected String message;
        protected Long total;
        protected List<T> data;

        public Convention() {

        }
    }
}
