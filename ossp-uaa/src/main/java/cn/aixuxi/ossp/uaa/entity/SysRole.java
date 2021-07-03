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
 * @date 2020/11/15 12:19
 */
@TableName("sys_role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysRole extends BaseEntity {

    /**
     * 角色标识程序中判断使用，如“admin”，这个是唯一的
     */
    private String role;
    /**
     * 角色描述，在UI界面显示使用
     */
    private String description;
    /**
     * 是否可用，如果不可用将不会添加给用户 1-可用 0-不可用
     */
    private Integer available = 1;
    /**
     * 备注
     */
    private String remark;

    @TableField(exist = false)
    private List<Integer> permIds = new ArrayList<>();
}
