package cn.aixuxi.ossp.uaa.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 用户服务
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-06 13:14
 **/
public interface OsspUserDetailsService extends UserDetailsService {

    /**
     * 根据电话号码查询用户
     * @param mobile 电话号码
     * @return 用户信息
     */
    UserDetails loadUserByMobile(String mobile);
}
