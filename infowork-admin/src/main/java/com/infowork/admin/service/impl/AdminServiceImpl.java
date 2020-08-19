package com.infowork.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.infowork.admin.dao.UmsAdminRoleRelationDao;
import com.infowork.admin.service.AdminService;
import com.infowork.common.domain.UserDTO;
import com.infowork.mbg.mapper.UmsAdminMapper;
import com.infowork.mbg.model.UmsAdmin;
import com.infowork.mbg.model.UmsAdminExample;
import com.infowork.mbg.model.UmsRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Create by wl on 2020/8/19
 */
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired(required = false)
    private UmsAdminMapper umsAdminMapper;
    @Autowired(required = false)
    private UmsAdminRoleRelationDao adminRoleRelationDao;
    @Override
    public UserDTO loadUserByUsername(String username) {
        UmsAdmin umsAdmin=getAdminByUsername(username);
        if (umsAdmin!=null) {
            List<UmsRole> umsRoleList=getRoleList(umsAdmin.getId());
            UserDTO userDTO=new UserDTO();
            BeanUtil.copyProperties(umsAdmin,userDTO);
            if (umsRoleList!=null &&umsRoleList.size()>0) {
                List<String> rolestr= umsRoleList.stream().map(role->role.getId()+"_"+role.getName()).collect(Collectors.toList());
                userDTO.setRoles(rolestr);
                return userDTO;
            }
        }
        return null;
    }

    @Override
    public UmsAdmin getAdminByUsername(String username) {
        UmsAdminExample umsAdminExample=new UmsAdminExample();
        umsAdminExample.createCriteria().andUsernameEqualTo(username);
        List<UmsAdmin> umsAdminList= umsAdminMapper.selectByExample(umsAdminExample);
        if (umsAdminList!=null && umsAdminList.size()>0) {
            return umsAdminList.get(0);
        }
        return null;
    }

    @Override
    public List<UmsRole> getRoleList(Long adminId) {
        List<UmsRole> umsRoleList= adminRoleRelationDao.getRoleList(adminId);
        return umsRoleList;
    }
}
