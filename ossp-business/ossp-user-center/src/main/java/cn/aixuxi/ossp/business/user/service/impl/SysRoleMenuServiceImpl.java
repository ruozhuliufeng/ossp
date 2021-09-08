package cn.aixuxi.ossp.business.user.service.impl;

import cn.aixuxi.ossp.business.user.mapper.SysRoleMapper;
import cn.aixuxi.ossp.business.user.mapper.SysRoleMenuMapper;
import cn.aixuxi.ossp.business.user.model.SysRoleMenu;
import cn.aixuxi.ossp.business.user.service.ISysRoleMenuService;
import cn.aixuxi.ossp.common.model.SysMenu;
import cn.aixuxi.ossp.common.model.SysRole;
import cn.aixuxi.ossp.common.service.impl.SuperServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-03 15:28
 **/
@Service
@Slf4j
public class SysRoleMenuServiceImpl extends SuperServiceImpl<SysRoleMenuMapper, SysRoleMenu>
                                    implements ISysRoleMenuService {
    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;
    /**
     * 保存角色菜单信息
     *
     * @param roleId 角色id
     * @param menuId 菜单id
     * @return 影响数
     */
    @Override
    public int save(Long roleId, Long menuId) {
        return sysRoleMenuMapper.save(roleId,menuId);
    }

    /**
     * 删除角色菜单信息
     *
     * @param roleId 角色id
     * @param menuId 菜单id
     * @return 影响数
     */
    @Override
    public int delete(Long roleId, Long menuId) {
        return sysRoleMenuMapper.delete(roleId,menuId);
    }

    /**
     * 根据角色id查询菜单集合
     *
     * @param roleIds 角色id集合
     * @param type    类型
     * @return 菜单集合
     */
    @Override
    public List<SysMenu> findMenusByRoleIds(Set<Long> roleIds, Integer type) {
        return sysRoleMenuMapper.findMenusByRoleIds(roleIds,type);
    }

    /**
     * 根据角色编码查询菜单集合
     *
     * @param roleCodes 角色编码集合
     * @param type      类型
     * @return 菜单集合
     */
    @Override
    public List<SysMenu> findMenusByRoleCodes(Set<String> roleCodes, Integer type) {
        return sysRoleMenuMapper.findMenusByRoleCodes(roleCodes,type);
    }
}
