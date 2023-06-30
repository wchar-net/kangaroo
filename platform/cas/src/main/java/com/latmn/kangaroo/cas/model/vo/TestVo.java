package com.latmn.kangaroo.cas.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "测试model", description = "测试model")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TestVo {

    @Schema(description = "uuid", example = "d845930cca2245208847d42417ae28c8")
    private String uuid;

    @Schema(description = "名称", example = "张三")
    private String name;
}
