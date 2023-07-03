package com.latmn.kangaroo.framework.core.result;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Result<T> implements Serializable {
    private String code;
    private String message;
    private String requestId;
    private String requestPath;
    private T data;
}
