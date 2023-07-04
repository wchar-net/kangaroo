package com.latmn.kangaroo.framework.core.domain;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserDomain {
    private String id;
    private String userCode;
    private String nickName;
    private LocalDateTime lastLogin;
    private Integer userState;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createBy;
    private String updateBy;
}
