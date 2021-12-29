package cn.aixuxi.ossp.common.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Set;

/**
 * 用户权限菜单实体
 * @author ruozhuliufeng
 * @date 2021-08-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_menu")
public class SysMenu extends BaseEntity{
    private static final long serialVersionUID = 749360940290141180L;
    /**
     * 上级菜单id
     */
    private Long parentId;

    /**
     * 菜单/权限名称
     */
    private String name;

    /**
     * 编码
     */
    private String css;

    /**
     * 地址
     */
    private String url;

    /**
     * 模块路径
     */
    private String path;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 类型 1-菜单 2-按钮/权限
     */
    private Integer type;

    /**
     * 是否隐藏
     */
    private Boolean hidden;

    /**
     * 请求的类型
     */
    private String pathMethod;
    /**
     * 租户字段
     */
    private String tenantId;

    /**
     * 上级菜单集合
     */
    @TableField(exist = false)
    private List<SysMenu> subMenus;

    /**
     * 角色Id
     */
    @TableField(exist = false)
    private Long roleId;


    /**
     * 菜单集合
     */
    @TableField(exist = false)
    private Set<Long> menuIds;
}
