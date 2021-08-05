package cn.aixuxi.ossp.common.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色信息
 * @author ruozhuliufeng
 * @date 2021-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_role")
public class SysRole extends BaseEntity{
    private static final long serialVersionUID = 4497149010220586111L;
    /**
     * 角色编码
     */
    private String code;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色描述
     */
    private String describe;
    /**
     * 用户id
     */
    @TableField(exist = false)
    private Long userId;
}
