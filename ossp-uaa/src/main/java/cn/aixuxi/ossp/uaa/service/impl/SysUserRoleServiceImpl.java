package cn.aixuxi.ossp.uaa.service.impl;

import cn.aixuxi.ossp.uaa.entity.SysUserRole;
import cn.aixuxi.ossp.uaa.mapper.SysUserRoleMapper;
import cn.aixuxi.ossp.uaa.service.SysUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类描述 用户-角色服务类，继承MP通用服务实现类，实现基础增删改查
 *
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021/4/14 21:35
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

    @Override
    public List<Integer> selectRoleByUserId(Integer id) {
        return getBaseMapper().selectRoleByUserId(id);
    }
}
