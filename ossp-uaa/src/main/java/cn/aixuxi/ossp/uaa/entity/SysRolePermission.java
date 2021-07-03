package cn.aixuxi.ossp.uaa.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 类描述
 *
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2020/11/15 12:26
 */
@TableName("sys_role_permission")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysRolePermission extends BaseEntity {

    private Integer roleId;
    private Integer permissionId;
}
