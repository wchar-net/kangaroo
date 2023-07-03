package com.latmn.kangaroo.platform.api.news.fallback;

import com.latmn.kangaroo.platform.api.news.NewsFeign;
import com.latmn.kangaroo.platform.api.news.domain.NewsTestDomain;
import com.latmn.kangaroo.framework.core.result.Result;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NewsFeignFallbackFactory implements FallbackFactory<NewsFeign> {
    @Override
    public NewsFeign create(Throwable cause) {
        return new NewsFeign() {
            @Override
            public Result<NewsTestDomain> sayHello() {
                Result result = new Result();
                result.setCode("0");
                result.setErrMessage("news调用失败! sayHello!");
                return result;
            }

            @Override
            public Result<List<NewsTestDomain>> sayHello2() {
                Result result = new Result();
                result.setCode("0");
                result.setErrMessage("news调用失败! sayHello2!");
                return result;
            }
        };
    }
}
