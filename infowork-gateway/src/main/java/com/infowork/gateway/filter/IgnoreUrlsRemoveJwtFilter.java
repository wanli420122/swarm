package com.infowork.gateway.filter;

import com.infowork.common.constant.AuthConstant;
import com.infowork.gateway.config.IgnoreUrlsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 白名单移除jwt token
 * Create by wl on 2020/8/20
 */
@Component
public class IgnoreUrlsRemoveJwtFilter implements WebFilter {
    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain fileterChain) {
        ServerHttpRequest request= exchange.getRequest();
        String uri=request.getURI().getPath();
        PathMatcher matcher=new AntPathMatcher();
        List<String> urls=ignoreUrlsConfig.getUrls();
        for(String uriStr :urls){
            if (matcher.match(uriStr,uri)) {
                //修改head中的jwt，设为空
                exchange.getRequest().mutate().header(AuthConstant.JWT_TOKEN_HEADER,"").build();
                return fileterChain.filter(exchange);
            }
        }
        return fileterChain.filter(exchange);
    }
}
