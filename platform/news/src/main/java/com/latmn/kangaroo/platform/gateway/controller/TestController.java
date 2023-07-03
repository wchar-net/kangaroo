package com.latmn.kangaroo.platform.gateway.controller;


import com.latmn.kangaroo.platform.api.news.domain.NewsTestDomain;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;


@Tag(name = "测试接口", description = "测试接口")
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/sayHello")
    public NewsTestDomain sayHello() {
        int s = 1 / 0;
        return NewsTestDomain.builder().id(5L).name("张安").build();
    }

    @GetMapping("/sayHello2")
    public List<NewsTestDomain> sayHello2() {
        return Arrays.asList(NewsTestDomain.builder().id(5L).name("张安").build(),
                NewsTestDomain.builder().id(50L).name("李四").build()
        );
    }
}
