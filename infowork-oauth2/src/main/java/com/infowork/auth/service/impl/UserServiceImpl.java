package com.infowork.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.infowork.auth.constant.MessageConstant;
import com.infowork.auth.domain.SecurityUser;
import com.infowork.auth.service.AdminService;
import com.infowork.common.constant.AuthConstant;
import com.infowork.common.domain.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;


/**
 * 用户服务实现类，实现UserDetailService重写loaduser方法，查询用户名对应用户信息
 * Create by wl on 2020/8/12
 */
@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private AdminService adminService;
    @Autowired
    private HttpServletRequest request;


/*    @PostConstruct
    public void initData() {
        String password = passwordEncoder.encode("123456");
        userList = new ArrayList<>();
        userList.add(new UserDTO(1L,"macro", password,1, CollUtil.toList("ADMIN")));
        userList.add(new UserDTO(2L,"andy", password,1, CollUtil.toList("TEST")));
    }*/

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String clientId = request.getParameter("client_id");
        UserDTO userDTO = null;
        if (clientId.equals(AuthConstant.ADMIN_CLIENT_ID)) {
            userDTO = adminService.loadUserByUsername(username);//查询用户名对应用户信息
        } else {
            throw new UsernameNotFoundException(MessageConstant.USERNAME_PASSWORD_ERROR);
        }
        userDTO.setClientId(clientId);
        SecurityUser securityUser = new SecurityUser(userDTO);
        if (!securityUser.isEnabled()) {
            throw new DisabledException(MessageConstant.ACCOUNT_DISABLED);//用户不可用
        } else if (!securityUser.isAccountNonExpired()) {
            throw new AccountExpiredException(MessageConstant.ACCOUNT_EXPIRED);//账号过期
        } else if (!securityUser.isAccountNonLocked()) {
            throw new LockedException(MessageConstant.ACCOUNT_LOCKED);//账号被锁定
        } else if (!securityUser.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException(MessageConstant.CREDENTIALS_EXPIRED);//账户的登录凭证已过期
        }
//        List<UserDTO> findUserList = userList.stream().filter(item -> item.getUsername().equals(username)).collect(Collectors.toList());
//        if (CollUtil.isEmpty(findUserList)) {
//            throw new UsernameNotFoundException(MessageConstant.USERNAME_PASSWORD_ERROR);
//        }
//        SecurityUser securityUser = new SecurityUser(findUserList.get(0));
//        if (!securityUser.isEnabled()) {
//            throw new DisabledException(MessageConstant.ACCOUNT_DISABLED);
//        } else if (!securityUser.isAccountNonLocked()) {
//            throw new LockedException(MessageConstant.ACCOUNT_LOCKED);
//        } else if (!securityUser.isAccountNonExpired()) {
//            throw new AccountExpiredException(MessageConstant.ACCOUNT_EXPIRED);
//        } else if (!securityUser.isCredentialsNonExpired()) {
//            throw new CredentialsExpiredException(MessageConstant.CREDENTIALS_EXPIRED);
//        }
        return securityUser;
    }

}
