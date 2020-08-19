package com.infowork.auth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * oauth2获取token返回的信息
 * Create by wl on 2020/8/19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class Oauth2TokenDto {
    @ApiModelProperty("访问令牌")
    private String token;

    @ApiModelProperty("刷新令牌")
    private String refreshToken;

    @ApiModelProperty("令牌头前缀")
    private String tokenHead;

    @ApiModelProperty("有效时间（秒）")
    private int expireIn;
}
