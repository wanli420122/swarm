package com.infowork.admin.service;

import com.infowork.common.domain.UserDTO;
import com.infowork.mbg.model.UmsAdmin;
import com.infowork.mbg.model.UmsRole;

import java.util.List;

/**
 * 后台管理员Service
 */
public interface AdminService {
    /**
     * 获取用户信息
     * @param username
     * @return
     */
    UserDTO loadUserByUsername(String username);
    /**
     * 根据用户名获取后台管理员
     */
    UmsAdmin getAdminByUsername(String username);

    /**
     * 根据管理员id获取对应角色信息
     * @param adminId
     * @return
     */
    List<UmsRole> getRoleList(Long adminId);
}
