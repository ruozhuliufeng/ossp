package cn.aixuxi.ossp.business.user.mapper;

import cn.aixuxi.ossp.business.user.model.SysRoleUser;
import cn.aixuxi.ossp.common.db.mapper.SuperMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户角色
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-03 15:16
 **/
@Mapper
public interface SysUserRoleMapper extends SuperMapper<SysRoleUser> {

    @Insert("INSERT INTO sys_role_user(user_id,role_id) VALUES(#{userId},#{roleId})")
    int saveUserRoles(@Param("userId") Long userId,@Param("roleId") Long roleId);

    int deleteUserRole(@Param("userId") Long userId,@Param("roleId") Long roleId);
}
