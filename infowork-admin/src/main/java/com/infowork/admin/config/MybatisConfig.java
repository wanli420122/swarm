package com.infowork.admin.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Mybatis配置类
 * Create by wl on 2020/8/21
 */
@Configuration
@EnableTransactionManagement
@MapperScan({
        "com.infowork.admin.dao",
        "com.infowork.mbg.mapper"
})
public class MybatisConfig {
}
