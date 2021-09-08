package cn.aixuxi.ossp.business.user.controller;

import cn.aixuxi.ossp.business.user.service.ISysRoleService;
import cn.aixuxi.ossp.common.model.PageResult;
import cn.aixuxi.ossp.common.model.Result;
import cn.aixuxi.ossp.common.model.SysRole;
import cn.aixuxi.ossp.common.utils.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 角色管理
 *
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-03 15:34
 **/
@RestController
@Slf4j
@Api(tags = "角色模块api")
public class SysRoleController {
    @Autowired
    private ISysRoleService sysRoleService;

    /**
     * 后台管理查询角色
     *
     * @param params 查询参数
     * @return 角色列表
     */
    @ApiOperation(value = "后台管理查询角色")
    @GetMapping("/roles")
    public PageResult<SysRole> findRoles(Map<String, Object> params) {
        return sysRoleService.findRoles(params);
    }

    /**
     * 用户管理查询所有角色
     *
     * @return 角色集合
     */
    @ApiOperation(value = "后台管理查询角色")
    @GetMapping("/allRoles")
    public Result<List<SysRole>> findAll() {
        List<SysRole> result = sysRoleService.findAll();
        return Result.succeed(result);
    }

    /**
     * 新增或修改角色
     *
     * @param sysRole 角色信息
     * @return Result
     * @throws Exception 异常
     */
    @PostMapping("/roles/saveOrUpdate")
    public Result saveOrUpdate(SysRole sysRole) throws Exception {
        return sysRoleService.saveOrUpdateRole(sysRole);
    }

    /**
     * 后台管理删除角色
     * @param id 角色id
     * @return Result
     */
    @ApiOperation(value = "后台管理删除角色")
    @DeleteMapping("/roles/{id}")
    public Result deleteRole(@PathVariable("id") Long id){
        try {
            if (id == 1L){
                return Result.failed("管理员不可以删除！");
            }
            sysRoleService.deleteRole(id);
            return Result.succeed();
        }catch (Exception e){
            log.error("角色删除失败！失败原因：{}",e.getMessage());
            return Result.failed();
        }
    }
}
