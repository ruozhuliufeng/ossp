package cn.aixuxi.ossp.business.user.service.impl;

import cn.aixuxi.ossp.business.user.mapper.SysMenuMapper;
import cn.aixuxi.ossp.business.user.model.SysRoleMenu;
import cn.aixuxi.ossp.business.user.service.ISysMenuService;
import cn.aixuxi.ossp.business.user.service.ISysRoleMenuService;
import cn.aixuxi.ossp.common.model.SysMenu;
import cn.aixuxi.ossp.common.service.impl.SuperServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-03 15:26
 **/
@Service
@Slf4j
public class SysMenuServiceImpl extends SuperServiceImpl<SysMenuMapper, SysMenu>
                            implements ISysMenuService {
    @Resource
    private ISysRoleMenuService roleMenuService;

    /**
     * 查询所有菜单
     *
     * @return 菜单列表
     */

    @Override
    public List<SysMenu> findAll() {
        return list();
//        return baseMapper.selectList(
//                new QueryWrapper<SysMenu>().orderByAsc("sort")
//        );
    }

    /**
     * 查询所有一级菜单
     *
     * @return 以及菜单集合
     */
    @Override
    public List<SysMenu> findOnes() {
        return baseMapper.selectList(
                new QueryWrapper<SysMenu>()
                        .eq("type",1)
                        .orderByAsc("sort")
        );
    }

    /**
     * 角色分配菜单
     *
     * @param roleId  角色id
     * @param menuIds 菜单id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void setMenuToRole(Long roleId, Set<Long> menuIds) {
        roleMenuService.delete(roleId,null);
        if (!CollectionUtils.isEmpty(menuIds)){
            List<SysRoleMenu> roleMenus = new ArrayList<>(menuIds.size());
            menuIds.forEach(menuId -> roleMenus.add(new SysRoleMenu(roleId,menuId)));
            roleMenuService.saveBatch(roleMenus);
        }
    }

    /**
     * 角色菜单列表
     *
     * @param roleIds 角色ids
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> findByRoles(Set<Long> roleIds) {
        return roleMenuService.findMenusByRoleIds(roleIds,null);
    }

    /**
     * 橘色菜单列表
     *
     * @param roleIds 角色ids
     * @param type    类型
     * @return 菜单列把你
     */
    @Override
    public List<SysMenu> findByRoles(Set<Long> roleIds, Integer type) {
        return roleMenuService.findMenusByRoleIds(roleIds,type);
    }

    /**
     * 角色菜单列表
     *
     * @param roleCodes 角色编码
     * @param type      类型
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> findByRoleCodes(Set<String> roleCodes, Integer type) {
        return roleMenuService.findMenusByRoleCodes(roleCodes,type);
    }
}
