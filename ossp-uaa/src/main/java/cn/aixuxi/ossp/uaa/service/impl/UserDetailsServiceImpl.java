package cn.aixuxi.ossp.uaa.service.impl;

import cn.aixuxi.ossp.common.feign.UserService;
import cn.aixuxi.ossp.common.model.LoginAppUser;
import cn.aixuxi.ossp.uaa.service.OsspUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户实现类
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-07 8:47
 **/
@Slf4j
@Service
public class UserDetailsServiceImpl implements OsspUserDetailsService, SocialUserDetailsService {

    @Resource
    private UserService userService;

    /**
     * 根据电话号码查询用户
     *
     * @param mobile 电话号码
     * @return 用户信息
     */
    @Override
    public UserDetails loadUserByMobile(String mobile) {
        LoginAppUser loginAppUser = userService.findByMobile(mobile);
        return checkUser(loginAppUser);
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginAppUser loginAppUser = userService.findByUsername(username);
        if (loginAppUser == null){
            throw new InternalAuthenticationServiceException("用户名或密码错误！");
        }
        return checkUser(loginAppUser);
    }

    @Override
    public SocialUserDetails loadUserByUserId(String openId) throws UsernameNotFoundException {
        LoginAppUser loginAppUser = userService.findByOpenId(openId);
        return checkUser(loginAppUser);
    }

    private LoginAppUser checkUser(LoginAppUser loginAppUser) {
        if (loginAppUser !=null && !loginAppUser.isEnabled()){
            throw new DisabledException("用户已作废");
        }
        return loginAppUser;
    }
}
