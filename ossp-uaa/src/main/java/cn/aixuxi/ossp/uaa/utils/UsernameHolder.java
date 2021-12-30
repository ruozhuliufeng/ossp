package cn.aixuxi.ossp.uaa.utils;

/**
 * 用户名holder
 * @author ruozhuliufeng
 */
public class UsernameHolder {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();
    public static String getContext(){
        return contextHolder.get();
    }
    public static void setContextHolder(String username){
        contextHolder.set(username);
    }
    public static void clearContext(){
        contextHolder.remove();
    }
}
