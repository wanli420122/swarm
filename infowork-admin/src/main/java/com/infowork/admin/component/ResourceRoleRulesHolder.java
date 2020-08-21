package com.infowork.admin.component;

import com.infowork.admin.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 角色资源加载类
 * Create by wl on 2020/8/21
 */
@Component
public class ResourceRoleRulesHolder {
    @Autowired
    private ResourceService resourceService;

    @PostConstruct
    public void initResourceRolesMap(){
        resourceService.initResourceRolesMap();
    }
}
