package cn.aixuxi.ossp.business.user.service;

import cn.aixuxi.ossp.common.model.SysMenu;
import cn.aixuxi.ossp.common.service.ISuperService;

import java.util.List;
import java.util.Set;

/**
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-03 15:21
 **/
public interface ISysMenuService extends ISuperService<SysMenu> {
    /**
     * 查询所有菜单
     *
     * @return 菜单列表
     */
    List<SysMenu> findAll();

    /**
     * 查询所有一级菜单
     *
     * @return 以及菜单集合
     */
    List<SysMenu> findOnes();

    /**
     * 角色分配菜单
     *
     * @param roleId  角色id
     * @param menuIds 菜单id
     */
    void setMenuToRole(Long roleId, Set<Long> menuIds);

    /**
     * 角色菜单列表
     *
     * @param roleIds 角色ids
     * @return 菜单列表
     */
    List<SysMenu> findByRoles(Set<Long> roleIds);

    /**
     * 橘色菜单列表
     *
     * @param roleIds 角色ids
     * @param type    类型
     * @return 菜单列把你
     */
    List<SysMenu> findByRoles(Set<Long> roleIds, Integer type);

    /**
     * 角色菜单列表
     *
     * @param roleCodes 角色编码
     * @param type      类型
     * @return 菜单列表
     */
    List<SysMenu> findByRoleCodes(Set<String> roleCodes, Integer type);
}
