package cn.aixuxi.ossp.uaa.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@TableName("sys_user")
public class SysUser extends BaseEntity {

    private String username;
    private String nickname;
    private String email;
    private String password;
    private String salt;
    private String phone;
    /**
     * 头像地址
     * @author : ruozhuliufeng
     * @date : 2021/6/14 17:06
     */
    private String avatar;
    private Integer statu;

    @TableField(exist = false)
    private List<SysRole> sysRoles = new ArrayList<>();
}
