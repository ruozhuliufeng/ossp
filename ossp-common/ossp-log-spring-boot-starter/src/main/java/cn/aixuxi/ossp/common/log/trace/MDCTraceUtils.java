package cn.aixuxi.ossp.common.log.trace;

import org.slf4j.MDC;

import java.util.Locale;
import java.util.UUID;

/**
 * 日志追踪工具类
 * @author ruozhuliufeng
 * @date 2021-07-17
 * 了解：MDC 全称是 Mapped Diagnostic Context，可以粗略的理解成是一个线程安全的存放诊断日志的容器。
 */
public class MDCTraceUtils {
    /**
     * 追踪id的名称
     */
    public static final String KEY_TRACE_ID = "traceId";

    /**
     * 日志链路追踪id信息头
     */
    public static final String TRACE_ID_HEADER = "x-traceId-header";

    /**
     * filter的优先级，值越低越优先
     */
    public static final int FILTER_ORDER = -1;

    /**
     * 创建traceId
     * @return traceId
     */
    public static String createTraceId(){
        return UUID.randomUUID().toString().replace("-","").toUpperCase();
    }
    /**
     * 创建traceId并赋值MDC
     */
    public static void addTraceId(){
        MDC.put(KEY_TRACE_ID,createTraceId());
    }

    /**
     * 赋值MDC
     */
    public static void putTraceId(String traceId){
        MDC.put(KEY_TRACE_ID,traceId);
    }

    /**
     * 获取MDC中的traceId的值
     * @return traceId的值
     */
    public static String getTraceId(){
        return MDC.get(KEY_TRACE_ID);
    }

    /**
     * 清除MDC中的值
     */
    public static void removeTraceId(){
        MDC.remove(KEY_TRACE_ID);
    }
}
