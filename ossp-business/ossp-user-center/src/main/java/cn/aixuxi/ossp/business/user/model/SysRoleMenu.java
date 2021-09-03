package cn.aixuxi.ossp.business.user.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

/**
 * 角色-资源菜单
 * @author ruozhuliufeng
 * @date 2021-09-03
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("sys_role_menu")
public class SysRoleMenu extends Model<SysRoleMenu> {
    private Long roleId;
    private Long menuId;
}
