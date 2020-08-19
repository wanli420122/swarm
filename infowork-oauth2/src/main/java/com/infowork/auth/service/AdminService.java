package com.infowork.auth.service;

import com.infowork.common.domain.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("infowork-admin")
public interface AdminService {
    @GetMapping("/admin/loadByUsername")
    UserDTO loadUserByUsername(@RequestParam String username);
}
