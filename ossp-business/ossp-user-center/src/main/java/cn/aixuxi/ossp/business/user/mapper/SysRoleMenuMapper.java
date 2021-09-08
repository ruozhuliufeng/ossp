package cn.aixuxi.ossp.business.user.mapper;

import cn.aixuxi.ossp.business.user.model.SysRoleMenu;
import cn.aixuxi.ossp.common.db.mapper.SuperMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色菜单
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-03 15:14
 **/
@Mapper
public interface SysRoleMenuMapper extends SuperMapper<SysRoleMenu> {
    int delete(Long roleId, Long menuId);
}
