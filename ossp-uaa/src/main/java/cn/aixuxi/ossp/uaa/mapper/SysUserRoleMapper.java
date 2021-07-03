package cn.aixuxi.ossp.uaa.mapper;

import cn.aixuxi.ossp.uaa.entity.SysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
    List<Integer> selectRoleByUserId(Integer id);
}
