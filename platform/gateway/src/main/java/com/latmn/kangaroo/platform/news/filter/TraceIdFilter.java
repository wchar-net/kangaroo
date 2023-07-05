package com.latmn.kangaroo.platform.news.filter;

import com.latmn.kangaroo.framework.core.util.TraceIdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ipresolver.XForwardedRemoteAddressResolver;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

import static org.springframework.cloud.gateway.filter.NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER;

@Component
public class TraceIdFilter implements GlobalFilter, Ordered {

    private final Logger logger = LoggerFactory.getLogger(TraceIdFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String traceId = TraceIdUtil.uuid32();
        ServerHttpRequest request = exchange.getRequest().mutate().header(TraceIdUtil.TRACE_ID, traceId).build();
        MDC.put(TraceIdUtil.TRACE_ID, traceId);
        XForwardedRemoteAddressResolver resolver = XForwardedRemoteAddressResolver.maxTrustedIndex(1);
        InetSocketAddress inetSocketAddress = resolver.resolve(exchange);
        String hostAddress = inetSocketAddress.getAddress().getHostAddress();
        logger.info("gateway new request ip: {} path: {}", hostAddress, request.getPath());
        return chain.filter(exchange.mutate().request(request).build());
    }

    @Override
    public int getOrder() {
        return WRITE_RESPONSE_FILTER_ORDER - 100;
    }
}
