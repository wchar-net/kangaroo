package com.latmn.kangaroo.framework.core.domain;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserDomain implements Serializable {
    private String id;
    private String userCode;
    private String nickName;
    private LocalDateTime lastLogin;
    private Integer userState;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime expireTime;
    private String createBy;
    private String updateBy;
}
