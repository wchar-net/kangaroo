package com.latmn.kangaroo.framework.core.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PageResult<T> implements Serializable {
    private String code;
    private String message;
    private String errMessage;
    private String requestId;
    private String requestPath;
    private Long total;
    private List<T> data;
    private T errData;
}
