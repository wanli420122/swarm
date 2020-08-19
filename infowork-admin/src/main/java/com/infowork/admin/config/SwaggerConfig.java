package com.infowork.admin.config;

import com.infowork.common.config.BaseSwaggerConfig;
import com.infowork.common.domain.SwaggerProperties;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger API文档相关配置
 * Create by wl on 2020/8/19
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {
    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("com.infowork.admin.controller")
                .title("infoworks后台系统")
                .description("infoworks后台相关接口文档")
                .contactName("wl")
                .version("1.0")
                .enableSecurity(true)
                .build();
    }
}
