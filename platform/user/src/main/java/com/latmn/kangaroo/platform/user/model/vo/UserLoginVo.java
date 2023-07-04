package com.latmn.kangaroo.platform.user.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(name = "登录", description = "登录")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserLoginVo {
    @NotEmpty(message = "用户编码不能为空")
    @Schema(description = "用户编码", requiredMode = REQUIRED)
    private String userCode;

    @NotEmpty(message = "用户密码不能为空")
    @Schema(description = "用户密码", requiredMode = REQUIRED)
    private String userPwd;

    @NotEmpty(message = "验证码不能为空")
    @Schema(description = "验证码header", requiredMode = REQUIRED)
    private String captchaHeader;

    @NotEmpty(message = "用户编码不能为空")
    @Schema(description = "验证码值", requiredMode = REQUIRED)
    private String captchaValue;
}
