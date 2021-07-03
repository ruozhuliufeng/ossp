package cn.aixuxi.ossp.uaa.service;

import cn.aixuxi.ossp.uaa.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

public interface SysUserService extends IService<SysUser> {

    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return 用户信息
     */
    SysUser selectByUsername(String username);

    /**
     * 根据用户id获取用户权限信息
     * @param userId 用户id
     * @return 用户的权限信息
     */
    String getUserAuthorityInfo(Integer userId);
    /**
     * 功能描述: 根据用户id，获取权限id
     * @param id 用户id
     * @return : java.util.List<java.lang.Integer>
     * @author : ruozhuliufeng
     * @date : 2021/6/14 22:14
     */
    Set<Integer> getNavIds(Integer id);

    /**
     * 功能描述: 根据权限id清除菜单缓存
     * @param id 权限id
     * @author : ruozhuliufeng
     * @date : 2021/6/14 23:48
     */
    void clearUserAuthorityInfoByPermId(Integer id);

    /**
     * 功能描述: 根据用户名清除权限缓存
     * @param username 用户名
     * @author : ruozhuliufeng
     * @date : 2021/6/14 23:49
     */
    void clearUserAuthorityInfo(String username);

    /**
     * 功能描述: 根据角色id清除权限缓存
     * @param roleId 角色id
     * @author : ruozhuliufeng
     * @date : 2021/6/14 23:49
     */
    void clearUserAuthorityInfoByRoleId(Integer roleId);

    /**
     * 获取权限列表
     * @param userId 用户id
     * @return 权限列表
     */
    Collection<? extends GrantedAuthority> getUserAuthority(Integer userId);
}
