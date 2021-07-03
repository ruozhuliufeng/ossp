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
 * @date 2020/11/15 12:27
 */
@TableName("sys_user_role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUserRole extends BaseEntity {

    private Integer uid;
    private Integer roleId;
}
