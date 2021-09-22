package cn.aixuxi.ossp.business.user.controller;

import cn.aixuxi.ossp.business.user.service.ISysMenuService;
import cn.aixuxi.ossp.common.constant.CommonConstant;
import cn.aixuxi.ossp.common.model.*;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单管理
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-03 15:34
 **/
@RestController
@Slf4j
@Api(tags = "菜单模块api")
@RequestMapping("/menus")
public class SysMenuController {
    @Autowired
    private ISysMenuService menuService;

    /**
     * 两层循环实现建树
     * @param sysMenus 菜单列表
     * @return 菜单列表
     */
    public static List<SysMenu> treeBuilder(List<SysMenu> sysMenus){
        List<SysMenu> menus = new ArrayList<>();
        for (SysMenu sysMenu:sysMenus){
            if (ObjectUtil.equal(-1L,sysMenu.getParentId())){
                menus.add(sysMenu);
            }
            for (SysMenu menu : sysMenus){
                if (menu.getParentId().equals(sysMenu.getId())){
                    if (sysMenu.getSubMenus() == null){
                        sysMenu.setSubMenus(new ArrayList<>());
                    }
                    sysMenu.getSubMenus().add(menu);
                }
            }
        }
        return menus;
    }

    /**
     * 删除菜单
     * @param id 菜单id
     * @return Result
     */
    @ApiOperation(value = "删除菜单")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id){
        try{
            menuService.removeById(id);
            return Result.succeed();
        }catch (Exception e){
            log.error("菜单删除失败！失败原因：{}",e.getMessage());
            return Result.failed();
        }
    }

    @ApiOperation(value = "根据roleId获取对应的菜单")
    @GetMapping("/{roleId}/menus")
    public List<Map<String,Object>> findMenusByRoleId(Long roleId){
        Set<Long> roleIds = new HashSet<>();
        roleIds.add(roleId);
        // 获取该角色对应的菜单
        List<SysMenu> roleMenus = menuService.findByRoles(roleIds);
        // 全部的菜单列表
        List<SysMenu> allMenus = menuService.findAll();
        List<Map<String,Object>> authTrees = new ArrayList<>();
        Map<Long,SysMenu> roleMenusMap = roleMenus.stream().collect(Collectors.toMap(SysMenu::getId,SysMenu->SysMenu));
        for (SysMenu sysMenu:allMenus){
            Map<String,Object> authTree = new HashMap<>();
            authTree.put("id",sysMenu.getId());
            authTree.put("name",sysMenu.getName());
            authTree.put("pId",sysMenu.getParentId());
            authTree.put("open",true);
            authTree.put("checked",false);
            if (roleMenusMap.get(sysMenu.getId())!=null){
                authTree.put("checked",true);
            }
            authTrees.add(authTree);
        }
        return authTrees;
    }

    @ApiOperation(value = "根据roleCodes获取对应的权限菜单")
    @SuppressWarnings("unchecked")
    @Cacheable(value = "menu",key = "#roleCodes")
    @GetMapping("/{roleCodes}")
    public List<SysMenu> findMenuByRoles(@PathVariable String roleCodes){
        List<SysMenu> result = null;
        if (StringUtils.isNotEmpty(roleCodes)){
            Set<String> roleSet = (Set<String>) Convert.toCollection(HashSet.class,String.class,roleCodes);
            result = menuService.findByRoleCodes(roleSet, CommonConstant.PERMISSION);
        }
        return result;
    }

    /**
     * 给角色分配菜单
     * @param sysMenu 角色-菜单信息
     * @return Result
     */
    @ApiOperation(value = "角色分配菜单")
    @PostMapping("/granted")
    public Result setMenuToRole(@RequestBody SysMenu sysMenu){
        menuService.setMenuToRole(sysMenu.getRoleId(),sysMenu.getMenuIds());
        return Result.succeed();
    }

    @ApiOperation(value = "查询所有菜单")
    @GetMapping("/findAlls")
    public PageResult<SysMenu> findAlls(){
        List<SysMenu> list = menuService.findAll();
        return PageResult.<SysMenu>builder().data(list).code(0).count((long) list.size()).build();
    }

    @ApiOperation(value = "获取菜单及顶级菜单")
    @GetMapping("/findOnes")
    public PageResult<SysMenu> findOnes(){
        List<SysMenu> list = menuService.findOnes();
        return PageResult.<SysMenu>builder().data(list).code(0).count((long) list.size()).build();
    }

    @ApiOperation(value = "新增或更新菜单")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody SysMenu sysMenu){
        try {
            menuService.saveOrUpdate(sysMenu);
            return Result.succeed();
        }catch (Exception e){
            log.error("菜单保存更新失败！失败原因：{}",e.getMessage());
            return Result.failed();
        }
    }

    @ApiOperation(value = "查询当前用户菜单")
    @GetMapping("/current")
    public List<SysMenu> findMenuByUser(SysUser user){
        List<SysRole> roles = user.getRoles();
        if (CollectionUtils.isEmpty(roles)){
            return Collections.emptyList();
        }
        List<SysMenu> menus = menuService.findByRoleCodes(roles.parallelStream().map(SysRole::getCode).collect(Collectors.toSet()),CommonConstant.MENU);
        return treeBuilder(menus);
    }



}
