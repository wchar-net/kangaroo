package com.latmn.kangaroo.platform.news.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.latmn.kangaroo.framework.core.define.Define;
import com.latmn.kangaroo.framework.core.result.Result;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class WriteUtil {

    public static Result authFailResult(String codeMsg, String errMsg, String requestId, String requestPath) {
        return Result.builder().code(codeMsg).errMessage(errMsg).requestId(requestId).requestPath(requestPath).build();
    }

    public static Mono<Void> error(ServerWebExchange exchange, Result result, HttpStatusCode status) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().put(Define.CONTENT_TYPE, Arrays.asList(MediaType.APPLICATION_JSON_VALUE));
        try {
            String json = new ObjectMapper().writeValueAsString(result);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));
            return exchange.getResponse().writeWith(Flux.just(buffer));
        } catch (IOException e) {
            e.printStackTrace();
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap("".getBytes(StandardCharsets.UTF_8));
            return exchange.getResponse().writeWith(Flux.just(buffer));
        }
    }

}
