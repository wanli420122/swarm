package com.infowork.admin.controller;

import com.infowork.admin.service.AdminService;
import com.infowork.common.api.CommonResult;
import com.infowork.common.domain.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户管理接口
 * Create by wl on 2020/8/19
 */
@Api(tags = "AdminController",description = "后台用户管理")
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Value("${feign.okhttp.enabled}")
    private String feginEnable;
    @ApiOperation("根据用户名获取通用用户信息")
    @GetMapping("/loadByUsername")
    public UserDTO loadUserByUsername(@RequestParam String username){
        UserDTO userDTO= adminService.loadUserByUsername(username);
        return userDTO;
    }
    @ApiOperation("测试nacos的动态获取config配置信息")
    @GetMapping("/testGetConfig")
    public CommonResult<String> getConfig(){
        return CommonResult.success(this.feginEnable);
    }
}
