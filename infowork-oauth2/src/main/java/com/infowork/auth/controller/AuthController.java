package com.infowork.auth.controller;


import com.infowork.auth.constant.MessageConstant;
import com.infowork.auth.domain.Oauth2TokenDto;
import com.infowork.common.api.CommonResult;
import com.infowork.common.constant.AuthConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.Map;

/**
 * oauth2获取令牌
 * Create by wl on 2020/8/19
 */
@RestController
@RequestMapping("/oauth")
@Api(tags="AuthController",description = "认证中心登录认证")
public class AuthController {
    @Autowired
    private TokenEndpoint tokenEndpoint;

    @ApiOperation("oauth2获取token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "grant_type", value = "授权模式", required = true),
            @ApiImplicitParam(name = "client_id", value = "Oauth2客户端ID", required = true),
            @ApiImplicitParam(name = "client_secret", value = "Oauth2客户端秘钥", required = true),
            @ApiImplicitParam(name = "refresh_token", value = "刷新token"),
            @ApiImplicitParam(name = "username", value = "登录用户名"),
            @ApiImplicitParam(name = "password", value = "登录密码")
    })
    @PostMapping("/oauth")
    public CommonResult<Oauth2TokenDto> postAccessToken(@ApiIgnore Principal principal,@ApiIgnore @RequestParam Map<String,String> parameters)
            throws HttpRequestMethodNotSupportedException {
        OAuth2AccessToken oAuth2AccessToken= (OAuth2AccessToken) tokenEndpoint.postAccessToken(principal,parameters);
        Oauth2TokenDto oauth2TokenDto=Oauth2TokenDto.builder()
                .token(oAuth2AccessToken.getValue())
                .refreshToken(oAuth2AccessToken.getRefreshToken().getValue())
                .tokenHead(AuthConstant.JWT_TOKEN_HEADER)
                .expireIn(oAuth2AccessToken.getExpiresIn()).build();
        return CommonResult.success(oauth2TokenDto);
    }

}
