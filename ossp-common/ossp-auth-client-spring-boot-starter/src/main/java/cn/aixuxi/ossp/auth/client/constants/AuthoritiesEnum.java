package cn.aixuxi.ossp.auth.client.constants;

/**
 * 权限常量
 * @author ruozhuliufeng
 * @date 2021-08-04
 */
public enum AuthoritiesEnum {

    /**
     * 管理员
     */
    ADMIN("ROLE_ADMIN"),

    /**
     * 普通用户
     */
    USER("ROLE_USER"),

    /**
     * 匿名用户
     */
    ANOYMOUS("ROLE_ANOYMOUS");

    private String role;

    AuthoritiesEnum(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}
