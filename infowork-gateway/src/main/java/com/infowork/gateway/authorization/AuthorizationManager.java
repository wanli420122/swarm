package com.infowork.gateway.authorization;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONUtil;
import com.infowork.common.constant.AuthConstant;
import com.infowork.common.domain.UserDTO;
import com.infowork.gateway.config.IgnoreUrlsConfig;
import com.nimbusds.jose.JWSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 鉴权管理器，用于判断是否有资源的访问权限
 * Created by wl on 2020/8/19.
 */
@Component
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        ServerHttpRequest request = authorizationContext.getExchange().getRequest();
        URI uri = request.getURI();
        String thisUrl = uri.getPath();
        PathMatcher matcher = new AntPathMatcher();
        //白名单放行
        List<String> urlLists = ignoreUrlsConfig.getUrls();
        for (String ignoreUrl : urlLists) {
            if (matcher.match(ignoreUrl, thisUrl)) {
                return Mono.just(new AuthorizationDecision(true));
            }
        }
        //跨域请求放行
        if (request.getHeaders().equals(HttpMethod.OPTIONS)) {
            return Mono.just(new AuthorizationDecision(true));
        }
        try {
            //获取请求头中的参数，
            String token = request.getHeaders().getFirst(AuthConstant.JWT_TOKEN_HEADER);
            //token为空拦截请求
            if (token == null) {
                return Mono.just(new AuthorizationDecision(false));
            }
            //解析jwt token
            String realToken = token.replace(AuthConstant.JWT_TOKEN_PREFIX, "");
            JWSObject jwsObject = JWSObject.parse(realToken);
            String userStr = jwsObject.getPayload().toString();
            UserDTO userDTO = JSONUtil.toBean(userStr, UserDTO.class);
            //非admin接口且clientid为admin的拦截
            if (userDTO.getClientId().equals(AuthConstant.ADMIN_CLIENT_ID) && !matcher.match(AuthConstant.ADMIN_URL_PATTERN, thisUrl)) {
                return Mono.just(new AuthorizationDecision(false));
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return Mono.just(new AuthorizationDecision(false));
        }
        //非admin接口直接放行
        if (!matcher.match(AuthConstant.ADMIN_URL_PATTERN, thisUrl)) {
            return Mono.just(new AuthorizationDecision(true));
        }
        //以下为admin内所有接口的权限校验
        //从Redis中获取当前路径可访问角色列表
        List<String> authorities = new ArrayList<>();
        Map<Object, Object> resourceRolesMap = redisTemplate.opsForHash().entries(AuthConstant.RESOURCE_ROLES_MAP_KEY);
        Iterator<Object> resourceRole = resourceRolesMap.keySet().iterator();
        while (resourceRole.hasNext()) {
            String pattern = (String) resourceRole.next();
            if (matcher.match(pattern, thisUrl)) {
                authorities.addAll(Convert.toList(String.class, resourceRolesMap.get(pattern)));
            }
        }
        //   authorities.stream().map(a->a=AuthConstant.AUTHORITY_PREFIX+a).collect(Collectors.toList());
//        List<String> authorities = Convert.toList(String.class,obj);
        //security默认在角色前加上ROLE_，所以每个角色标识拼上ROLE_
        authorities = authorities.stream().map(i -> i = AuthConstant.AUTHORITY_PREFIX + i).collect(Collectors.toList());
        //认证通过且角色匹配的用户可访问当前路径
        return mono
                .filter(Authentication::isAuthenticated)
                .flatMapIterable(Authentication::getAuthorities)
                .map(GrantedAuthority::getAuthority)
                .any(authorities::contains)
                .map(AuthorizationDecision::new)
                .defaultIfEmpty(new AuthorizationDecision(false));
    }

}
