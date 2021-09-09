package cn.aixuxi.ossp.business.user.mapper;

import cn.aixuxi.ossp.business.user.model.SysRoleUser;
import cn.aixuxi.ossp.common.db.mapper.SuperMapper;
import cn.aixuxi.ossp.common.model.SysRole;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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

    List<SysRole> findRolesByUserId(@Param("userId") Long userId);

    @Select("<script>select r.*,ru.user_id from sys_role_user ru inner join sys_role r on r.id = ru.role_id where ru.user_id IN " +
            " <foreach item='item' index='index' collection='list' open='(' separator=',' close=')'> " +
            " #{item} " +
            " </foreach>" +
            "</script>")
    List<SysRole> findRolesByUserIds(List<Long> userIds);
}
