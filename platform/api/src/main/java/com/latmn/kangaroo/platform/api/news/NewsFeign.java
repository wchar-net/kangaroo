package com.latmn.kangaroo.platform.api.news;

import com.latmn.kangaroo.framework.core.result.PageResult;
import com.latmn.kangaroo.platform.api.news.domain.NewsTestDomain;
import com.latmn.kangaroo.platform.api.news.fallback.NewsFeignFallbackFactory;
import com.latmn.kangaroo.framework.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "newsFeign", value = "news", path = "/news", fallbackFactory = NewsFeignFallbackFactory.class)
public interface NewsFeign {
    @GetMapping("/test/sayHello")
    Result<NewsTestDomain> sayHello();

    @GetMapping("/test/sayHello2")
    Result<List<NewsTestDomain>> sayHello2();

    @GetMapping("/test/page")
    PageResult<NewsTestDomain> page(
            @RequestParam(defaultValue = "1") Integer pageIndex,
            @RequestParam(defaultValue = "100") Integer pageSize
    );
}


