package com.latmn.kangaroo.api.news;

import com.latmn.kangaroo.api.news.domain.NewsTestDomain;
import com.latmn.kangaroo.framework.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(contextId = "newsFeign", value = "news", path = "/news")
public interface NewsFeign {
    @GetMapping("/test/sayHello")
    Result<NewsTestDomain> sayHello();

    @GetMapping("/test/sayHello2")
    Result<List<NewsTestDomain>> sayHello2();
}
