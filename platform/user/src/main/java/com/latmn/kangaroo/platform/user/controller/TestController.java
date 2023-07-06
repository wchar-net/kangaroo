package com.latmn.kangaroo.platform.user.controller;

import com.latmn.kangaroo.framework.core.define.Define;
import com.latmn.kangaroo.framework.core.domain.UserDomain;
import com.latmn.kangaroo.framework.core.result.PageResult;
import com.latmn.kangaroo.platform.api.news.NewsFeign;
import com.latmn.kangaroo.platform.api.news.domain.NewsTestDomain;
import com.latmn.kangaroo.framework.core.result.Result;
import com.latmn.kangaroo.platform.user.model.vo.TestVo;
import com.latmn.kangaroo.platform.user.service.TestService;
import com.latmn.kangaroo.platform.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "测试接口", description = "测试接口")
@RestController
@RequestMapping("/test")
public class TestController {

    private final TestService testService;
    private final NewsFeign newsFeign;
    private final UserService userService;

    public TestController(TestService testService, NewsFeign newsFeign, UserService userService) {
        this.testService = testService;
        this.newsFeign = newsFeign;
        this.userService = userService;
    }

    @GetMapping("/test003")
    public UserDomain test003(HttpServletRequest request) {
        String userId = request.getHeader(Define.USER_REQUEST_KEY);
        return userService.getUserInfo(userId);
    }

    @GetMapping("/testFeign2")
    public PageResult<NewsTestDomain> testFeign2() {
        PageResult<NewsTestDomain> page = newsFeign.page(2, 50);
        return page;
    }


    @GetMapping("/testFeign")
    public NewsTestDomain testFeign() {
        Result<NewsTestDomain> result = newsFeign.sayHello();
        if (!"1".equalsIgnoreCase(result.getCode())) {
            throw new RuntimeException(result.getErrMessage());
        }
        return result.getData();
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
