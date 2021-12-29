package cn.aixuxi.ossp.common.log.trace;

import cn.hutool.core.util.RandomUtil;
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
     * 块ID的名称
     */
    public static final String KEY_SPAN_ID = "spanId";

    /**
     * 父块ID的名称
     */
    public static final String KEY_PARENT_ID = "parentId";
    /**
     * 日志链路追踪id信息头
     */
    public static final String TRACE_ID_HEADER = "x-traceId-header";

    /**
     * 日志链路块ID信息头
     */
    public static final String SPAN_ID_HEADER = "x-spanId-header";
    /**
     * 日志链路父块ID信息头
     */
    public static final String PARENT_ID_HEADER = "x-parentId-header";

    /**
     * filter的优先级，值越低越优先
     */
    public static final int FILTER_ORDER = -1;

    /**
     * 创建traceId
     * @return traceId
     */
    public static String createTraceId(){
        return RandomUtil.randomString(16);
    }
    /**
     * 创建traceId并赋值MDC
     */
    public static void addTraceId(){
        String traceId = createTraceId();
        MDC.put(KEY_TRACE_ID,traceId);
        MDC.put(KEY_SPAN_ID,traceId);
    }

    /**
     * 赋值MDC
     */
    public static void putTraceId(String traceId,String spanId){
        MDC.put(KEY_TRACE_ID,traceId);
        MDC.put(KEY_PARENT_ID,spanId);
        MDC.put(KEY_SPAN_ID,createTraceId());
    }

    /**
     * 获取MDC中的spanId值
     */
    public static String getSpanId(){
        return MDC.get(KEY_SPAN_ID);
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
        MDC.remove(KEY_SPAN_ID);
        MDC.remove(KEY_PARENT_ID);
    }
}
