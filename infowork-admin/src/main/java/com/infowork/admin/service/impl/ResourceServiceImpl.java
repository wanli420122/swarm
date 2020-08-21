package com.infowork.admin.service.impl;

import com.infowork.admin.service.ResourceService;
import com.infowork.common.constant.AuthConstant;
import com.infowork.common.service.RedisService;
import com.infowork.mbg.mapper.UmsResourceMapper;
import com.infowork.mbg.mapper.UmsRoleMapper;
import com.infowork.mbg.mapper.UmsRoleResourceRelationMapper;
import com.infowork.mbg.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Create by wl on 2020/8/21
 */
@Service
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    private UmsResourceMapper resourceMapper;
    @Autowired
    private UmsRoleMapper roleMapper;
    @Autowired
    private UmsRoleResourceRelationMapper roleResourceRelationMapper;
    @Autowired
    private RedisService redisService;
    @Value("${spring.application.name}")
    private String applicationName;
    @Override
    public Map<String, List<String>> initResourceRolesMap() {
        Map<String, List<String>> resourceRolesMap =new TreeMap<>();
        List<UmsResource> resources= resourceMapper.selectByExample(new UmsResourceExample());
        List<UmsRole> roles=roleMapper.selectByExample(new UmsRoleExample());
        List<UmsRoleResourceRelation> resourceRelations=roleResourceRelationMapper.selectByExample(new UmsRoleResourceRelationExample());
        for (UmsResource umsResource : resources){
            //获取改资源对应的所有关联角色信息
            Set<Long> rolesId=resourceRelations.stream().filter(item->item.getResourceId().equals(umsResource.getId())).map(r->r.getRoleId()).collect(Collectors.toSet());
            List<String> rolesName=roles.stream().filter(item->rolesId.contains(item.getId())).map(r->r.getId()+"_"+r.getName()).collect(Collectors.toList());
            resourceRolesMap.put("/"+applicationName+umsResource.getUrl(),rolesName);
        }
        redisService.del(AuthConstant.RESOURCE_ROLES_MAP_KEY);
        redisService.hSetAll(AuthConstant.RESOURCE_ROLES_MAP_KEY,resourceRolesMap);
        return resourceRolesMap;
    }
}
