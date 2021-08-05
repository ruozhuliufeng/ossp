package cn.aixuxi.ossp.common.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_user")
public class SysUser extends BaseEntity {
    private static final long serialVersionUID = -5886012896705137070L;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像地址
     */
    private String headImgUrl;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 性别 1-男 2-女
     */
    private Integer sex;

    /**
     * 是否可用
     */
    private Boolean enabled;

    /**
     * 人员类型
     */
    private String type;

    /**
     * 开放平台id标识
     */
    private String openId;

    /**
     * 逻辑删除标识
     */
    @TableLogic
    private boolean isDel;

    /**
     * 角色信息集合
     */
    @TableField(exist = false)
    private List<SysRole> roles;
    @TableField(exist = false)
    private String roleId;
    /**
     * 旧密码
     */
    @TableField(exist = false)
    private String oldPassword;
    /**
     * 新密码
     */
    @TableField(exist = false)
    private String newPassword;
}
