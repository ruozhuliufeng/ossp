package cn.aixuxi.ossp.business.user.service;

import cn.aixuxi.ossp.business.user.model.SysRoleMenu;
import cn.aixuxi.ossp.common.model.SysMenu;
import cn.aixuxi.ossp.common.service.ISuperService;

import java.util.List;
import java.util.Set;

/**
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-03 15:21
 **/
public interface ISysRoleMenuService extends ISuperService<SysRoleMenu> {
    /**
     * 保存角色菜单信息
     * @param roleId  角色id
     * @param menuId 菜单id
     * @return 影响数
     */
    int save(Long roleId,Long menuId);

    /**
     * 删除角色菜单信息
     * @param roleId 角色id
     * @param menuId 菜单id
     * @return 影响数
     */
    int delete(Long roleId,Long menuId);

    /**
     * 根据角色id查询菜单集合
     * @param roleIds 角色id集合
     * @param type 类型
     * @return 菜单集合
     */
    List<SysMenu> findMenusByRoleIds(Set<Long> roleIds, Integer type);

    /**
     * 根据角色编码查询菜单集合
     * @param roleCodes 角色编码集合
     * @param type 类型
     * @return 菜单集合
     */
    List<SysMenu> findMenusByRoleCodes(Set<String> roleCodes,Integer type);
}
