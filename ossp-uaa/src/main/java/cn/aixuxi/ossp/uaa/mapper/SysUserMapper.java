package cn.aixuxi.ossp.uaa.mapper;

import cn.aixuxi.ossp.uaa.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface SysUserMapper extends BaseMapper<SysUser> {
    /**
     * 根据权限id获取用户列表
     * @param id 权限id
     * @return 用户列表
     */
    List<SysUser> listByPermId(Integer id);
}
