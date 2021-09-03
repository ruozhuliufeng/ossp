package cn.aixuxi.ossp.business.user.mapper;

import cn.aixuxi.ossp.common.db.mapper.SuperMapper;
import cn.aixuxi.ossp.common.model.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-03 15:15
 **/
@Mapper
public interface SysUserMapper extends SuperMapper<SysUser> {
}
