package com.latmn.kangaroo.platform.user.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(name = "验证码", description = "验证码")
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaptchaVo {
    private String base64Img;
    private String captchaHeader;
}
