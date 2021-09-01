package cn.aixuxi.ossp.common.context;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * 负载均衡策略Holder
 * @author ruozhuliufeng
 * @date 2021-09-01
 */
public class LbIsolationContextHolder {
    private static final ThreadLocal<String> VERSION_CONTEXT = new TransmittableThreadLocal<>();

    public static void setVersion(String version){
        VERSION_CONTEXT.set(version);
    }

    public static String getVersion(){
        return VERSION_CONTEXT.get();
    }

    public static void clear(){
        VERSION_CONTEXT.remove();
    }
}
