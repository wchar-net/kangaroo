package com.latmn.kangaroo.cas.controller;

import com.latmn.kangaroo.cas.model.vo.TestVo;
import com.latmn.kangaroo.cas.service.TestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "测试接口", description = "测试接口")
@RestController
@RequestMapping("/test")
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @Operation(summary = "获取所有测试数据")
    @GetMapping("/findAll")
    public List<TestVo> findAll() {
        return testService.findAll();
    }

    @Operation(summary = "根据id获取数据")
    @Parameters({
            @Parameter(name = "id", description = "id", required = true),
    })
    @GetMapping("/findTestById")
    public TestVo findTestById(String id) {
        return testService.findTestById(id);
    }

    @Operation(summary = "保存数据")
    @PostMapping("/saveTest")
    public TestVo findTestById(@RequestBody @Parameter(name = "vo", required = true) TestVo vo) {
        return vo;
    }
}