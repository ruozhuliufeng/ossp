package cn.aixuxi.ossp.uaa.service.impl;

import cn.aixuxi.ossp.uaa.entity.SysRole;
import cn.aixuxi.ossp.uaa.mapper.SysRoleMapper;
import cn.aixuxi.ossp.uaa.service.SysRoleService;
import cn.aixuxi.ossp.uaa.service.SysUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述 角色服务类，继承MP通用服务实现类，实现基础增删改查
 *
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021/4/14 21:35
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysUserRoleService userRoleService;

    /**
     * 功能描述: 根据用户id获取角色id列表
     *
     * @param id 用户id
     * @return : 角色id列表
     * @author : ruozhuliufeng
     * @date : 2021/6/14 20:35
     */
    @Override
    public List<SysRole> listRolesByUserId(Integer id) {
        List<Integer> roleIds = userRoleService.selectRoleByUserId(id);
        return roleIds.stream().map(this::getById).collect(Collectors.toList());
    }
}
