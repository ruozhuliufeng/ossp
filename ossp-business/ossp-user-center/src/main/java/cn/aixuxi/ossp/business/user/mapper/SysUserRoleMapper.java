package cn.aixuxi.ossp.business.user.mapper;

import cn.aixuxi.ossp.business.user.model.SysRoleUser;
import cn.aixuxi.ossp.common.db.mapper.SuperMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户角色
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-03 15:16
 **/
@Mapper
public interface SysUserRoleMapper extends SuperMapper<SysRoleUser> {
}
