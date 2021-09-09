package cn.aixuxi.ossp.business.user.service.impl;

import cn.aixuxi.ossp.business.user.mapper.SysRoleMapper;
import cn.aixuxi.ossp.business.user.mapper.SysRoleMenuMapper;
import cn.aixuxi.ossp.business.user.mapper.SysUserRoleMapper;
import cn.aixuxi.ossp.business.user.service.ISysRoleService;
import cn.aixuxi.ossp.common.lock.DistributedLock;
import cn.aixuxi.ossp.common.model.PageResult;
import cn.aixuxi.ossp.common.model.Result;
import cn.aixuxi.ossp.common.model.SysRole;
import cn.aixuxi.ossp.common.service.impl.SuperServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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

    private final static String LOCK_KEY_ROLECODE = "rolecode:";

    @Resource
    private SysUserRoleMapper userRoleMapper;
    @Resource
    private SysRoleMenuMapper roleMenuMapper;
    @Autowired
    private DistributedLock lock;

    /**
     * 保存角色信息
     *
     * @param sysRole 角色信息
     * @throws Exception 异常
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveRole(SysRole sysRole) throws Exception {
        String roleCode = sysRole.getCode();
        super.saveIdempotency(sysRole,lock,
                LOCK_KEY_ROLECODE+roleCode,
                new QueryWrapper<SysRole>().eq("code",roleCode),
                "角色code已存在");
    }

    /**
     * 根据id删除角色信息
     *
     * @param id 角色id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteRole(Long id) {
        baseMapper.deleteById(id);
        // 删除角色对应菜单信息
        roleMenuMapper.delete(id,null);
        // 删除用户-角色信息
        userRoleMapper.deleteUserRole(null,id);
    }

    /**
     * 角色列表
     *
     * @param params 查询参数
     * @return 角色列表
     */
    @Override
    public PageResult<SysRole> findRoles(Map<String, Object> params) {
        Integer curPage = MapUtils.getInteger(params, "page");
        Integer limit = MapUtils.getInteger(params, "limit");
        Page<SysRole> page = new Page<>(curPage == null ? 0 : curPage, limit == null ? -1 : limit);
        List<SysRole> list = baseMapper.findList(page, params);
        return PageResult.<SysRole>builder().data(list).code(0).count(page.getTotal()).build();
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
        if (sysRole.getId() == null) {
            this.saveRole(sysRole);
        } else {
            baseMapper.updateById(sysRole);
        }
        return Result.succeed();
    }

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    @Override
    public List<SysRole> findAll() {
        return baseMapper.findAll();
    }
}
