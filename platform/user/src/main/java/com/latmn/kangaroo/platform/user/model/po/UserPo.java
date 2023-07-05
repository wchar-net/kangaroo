package com.latmn.kangaroo.platform.user.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@TableName("rbac_user")
public class UserPo {
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField(value = "user_code")
    private String userCode;

    @TableField(value = "nick_name")
    private String nickName;

    @TableField(value = "last_login")
    private LocalDateTime lastLogin;

    @TableField(value = "user_state")
    private Integer userState;

    @TableField(value = "user_pwd")
    private String userPwd;

    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    @TableField(value = "create_by")
    private String createBy;

    @TableField(value = "update_by")
    private String updateBy;

    @TableField(value = "expire_time")
    private LocalDateTime expireTime;

    @TableField(value = "del_flag")
    private Integer delFlag;
}
