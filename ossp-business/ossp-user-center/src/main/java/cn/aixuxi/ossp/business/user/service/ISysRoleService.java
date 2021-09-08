package cn.aixuxi.ossp.business.user.service;

import cn.aixuxi.ossp.common.model.PageResult;
import cn.aixuxi.ossp.common.model.Result;
import cn.aixuxi.ossp.common.model.SysRole;
import cn.aixuxi.ossp.common.service.ISuperService;

import java.util.List;
import java.util.Map;

/**
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-03 15:22
 **/
public interface ISysRoleService extends ISuperService<SysRole> {

    /**
     * 保存角色信息
     * @param sysRole 角色信息
     * @throws Exception 异常
     */
    void saveRole(SysRole sysRole) throws Exception;

    /**
     * 根据id删除角色信息
     * @param id
     */
    void deleteRole(Long id);

    /**
     * 角色列表
     * @param params 查询参数
     * @return 角色列表
     */
    PageResult<SysRole> findRoles(Map<String,Object> params);

    /**
     * 新增或更新角色
     * @param sysRole 角色信息
     * @return Result
     * @throws Exception 异常
     */
    Result saveOrUpdateRole(SysRole sysRole) throws Exception;

    /**
     * 查询所有角色
     * @return 角色列表
     */
    List<SysRole> findAll();
}
