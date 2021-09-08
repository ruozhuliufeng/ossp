package cn.aixuxi.ossp.business.user.service.impl;

import cn.aixuxi.ossp.business.user.mapper.SysRoleMapper;
import cn.aixuxi.ossp.business.user.service.ISysRoleService;
import cn.aixuxi.ossp.common.model.PageResult;
import cn.aixuxi.ossp.common.model.Result;
import cn.aixuxi.ossp.common.model.SysRole;
import cn.aixuxi.ossp.common.service.impl.SuperServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-03 15:29
 **/
@Slf4j
@Service
public class SysRoleServiceImpl extends SuperServiceImpl<SysRoleMapper, SysRole>
                                implements ISysRoleService {
    /**
     * 保存角色信息
     *
     * @param sysRole 角色信息
     * @throws Exception 异常
     */
    @Override
    public void saveRole(SysRole sysRole) throws Exception {

    }

    /**
     * 根据id删除角色信息
     *
     * @param id
     */
    @Override
    public void deleteRole(Long id) {

    }

    /**
     * 角色列表
     *
     * @param params 查询参数
     * @return 角色列表
     */
    @Override
    public PageResult<SysRole> findRoles(Map<String, Object> params) {
        return null;
    }

    /**
     * 新增或更新角色
     *
     * @param sysRole 角色信息
     * @return Result
     * @throws Exception 异常
     */
    @Override
    public Result saveOrUpdateRole(SysRole sysRole) throws Exception {
        return null;
    }

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    @Override
    public List<SysRole> findAll() {
        return null;
    }
}
