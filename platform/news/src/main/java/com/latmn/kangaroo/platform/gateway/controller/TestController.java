package com.latmn.kangaroo.platform.gateway.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.latmn.kangaroo.framework.core.result.PageResult;
import com.latmn.kangaroo.platform.api.news.domain.NewsTestDomain;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/page")
    public PageResult<NewsTestDomain> page(
            @RequestParam(defaultValue = "1") Integer pageIndex,
            @RequestParam(defaultValue = "100") Integer pageSize
    ) {
        PageResult<NewsTestDomain> pageResult = new PageResult<>();
        pageResult.setMessage("pageIndex = " + pageIndex + "  pageSize= " + pageSize);
        List<NewsTestDomain> list = Arrays.asList(NewsTestDomain.builder().id(5L).name("张安").build(),
                NewsTestDomain.builder().id(50L).name("李四").build()
        );
        pageResult.setTotal((long) list.size());
        pageResult.setData(list);
        return pageResult;
    }

    @GetMapping("/sayHello2")
    public List<NewsTestDomain> sayHello2() {
        return Arrays.asList(NewsTestDomain.builder().id(5L).name("张安").build(),
                NewsTestDomain.builder().id(50L).name("李四").build()
        );
    }
}
