package cn.aixuxi.ossp.uaa.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;

/**
 * 用户服务
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-06 13:14
 **/
public interface OsspUserDetailsService extends UserDetailsService {

    /**
     * 判断实现类是否属于该类型
     * @param accountType 账号类型
     * @return 是否支持
     */
    boolean supports(String accountType);

    /**
     * 根据电话号码查询用户
     * @param mobile 电话号码
     * @return 用户信息
     */
    UserDetails loadUserByMobile(String mobile);

    /**
     * 根据用户id/openid查询用户
     * @param userId 用户id
     * @return 用户信息
     * @throws UsernameNotFoundException 异常
     */
    SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException;
}
