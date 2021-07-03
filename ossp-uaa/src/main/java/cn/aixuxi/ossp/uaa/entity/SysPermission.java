package cn.aixuxi.ossp.uaa.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述
 *
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2020/11/15 12:21
 */
@TableName("sys_permission")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysPermission extends BaseEntity{

    /**
     * 名称
     */
    private String name;
    /**
     * 资源类型：menu|button
     */
    private Integer resourceType;
    /**
     * 资源路径
     */
    private String path;
    /**
     * 权限字符串 munu例子：role:*, button例子：role:create,role:update,role:delete,role:view
     */
    private String permission;
    /**
     * 上级菜单，一级菜单为0
     */
    private Integer parentId;
    /**
     * 父编号列表
     */
    private String parentIds;
    /**
     * 是否可用，如果不可用将不会添加给用户 0-可用 1-不可用
     */
    private Integer available;
    /**
     * 菜单图标
     */
    private String icon;
    /**
     * 组件
     */
    private String component;
    /**
     * 排序
     */
    private Integer orderNum;
    /**
     * 数据库中不存在，不映射
     */
    @TableField(exist = false)
    private List<SysPermission> children = new ArrayList<>();
}
