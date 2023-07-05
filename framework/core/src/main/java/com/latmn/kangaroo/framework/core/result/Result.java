package com.latmn.kangaroo.framework.core.result;


import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Result<T> implements Serializable {
    private String code;
    private String message;
    private String errMessage;
    private String requestId;
    private String requestPath;
    private T data;
    private Object errData;
}
