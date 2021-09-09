package cn.aixuxi.ossp.business.user.mapper;

import cn.aixuxi.ossp.business.user.model.SysRoleMenu;
import cn.aixuxi.ossp.common.db.mapper.SuperMapper;
import cn.aixuxi.ossp.common.model.SysMenu;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 角色菜单
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-03 15:14
 **/
@Mapper
public interface SysRoleMenuMapper extends SuperMapper<SysRoleMenu> {
    int delete(@Param("roleId") Long roleId,@Param("menuId") Long menuId);

    List<SysMenu> findMenusByRoleIds(@Param("roleIds") Set<Long> roleIds,@Param("type") Integer type);

    List<SysMenu> findMenusByRoleCodes(@Param("roleCodes") Set<String> roleCodes,@Param("type") Integer type);

    @Insert("INSERT INTO sys_role_menu(role_id,menu_id) values(#{roleId},#{menuId})")
    int save(@Param("roleId") Long roleId,@Param("menuId") Long menuId);
}
