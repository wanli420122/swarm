package com.infowork.admin.service;

import java.util.List;
import java.util.Map;

/**
 * 后台管理资源
 * 2020年8月21日09:19:09
 * wl
 */
public interface ResourceService {
    /**
     * 初始化资源角色规则
     */
    Map<String, List<String>> initResourceRolesMap();
}
