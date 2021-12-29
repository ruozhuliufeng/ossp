package cn.aixuxi.ossp.common.log.trace;

import cn.aixuxi.ossp.common.log.properties.TraceProperties;
import feign.RequestInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * feign拦截器，传递traceId
 * @author ruozhuliufeng
 * @date 2021-07-17
 * 注解注释1：@Conditional：满足特定条件创建一个bean
 * 注解注释2：@ConditionalOnClass：满足当前类路径下有指定的类的条件下创建一个bean
 */
@Configuration
@ConditionalOnClass(value = {RequestInterceptor.class})
public class FeignTraceConfig {
    @Resource
    private TraceProperties traceProperties;

    @Bean
    public RequestInterceptor feignTraceInterceptor(){
        return requestTemplate -> {
            if (traceProperties.getEnable()){
                // 传递日志traceId
                String traceId = MDCTraceUtils.getTraceId();
                if (!StringUtils.isEmpty(traceId)){
                    String spanId = MDCTraceUtils.getSpanId();
                    requestTemplate.header(MDCTraceUtils.TRACE_ID_HEADER,traceId);
                    requestTemplate.header(MDCTraceUtils.SPAN_ID_HEADER,spanId);
                }
            }
        };
    }
}
