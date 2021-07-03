package cn.aixuxi.ossp.uaa.service.impl;

import cn.aixuxi.ossp.uaa.entity.SysUser;
import cn.aixuxi.ossp.uaa.service.SysUserService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 类描述
 *
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021/4/14 21:35
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询数据
        SysUser sysUser = sysUserService.selectByUsername(username);
        // 判断用户是否为空
        if (sysUser == null){
            throw new UsernameNotFoundException("用户不存在");
        }
        // 将用户信息转为JSON存储
        String principal = JSON.toJSONString(sysUser);
        return User.withUsername(principal).password(sysUser.getPassword())
                .authorities(sysUserService.getUserAuthority(sysUser.getId())).build();
    }
}
