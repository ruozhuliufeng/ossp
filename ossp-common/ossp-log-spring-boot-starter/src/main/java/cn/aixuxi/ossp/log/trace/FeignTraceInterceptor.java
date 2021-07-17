package cn.aixuxi.ossp.log.trace;

import cn.aixuxi.ossp.log.properties.TraceProperties;
import feign.RequestInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * feign拦截器，传递traceId
 * @author ruozhuliufeng
 * @date 2021-07-17
 * 注解注释1：@Conditional：满足特定条件创建一个bean
 * 注解注释2：@ConditionalOnClass：满足当前类路径下有指定的类的条件下创建一个bean
 */
@ConditionalOnClass(value = {RequestInterceptor.class})
public class FeignTraceInterceptor {
    @Resource
    private TraceProperties traceProperties;

    @Bean
    public RequestInterceptor feignTraceInterceptor(){
        return requestTemplate -> {
            if (traceProperties.getEnable()){
                // 传递日志traceId
                String traceId = MDCTraceUtils.getTraceId();
                if (!StringUtils.isEmpty(traceId)){
                    requestTemplate.header(MDCTraceUtils.TRACE_ID_HEADER,traceId);
                }
            }
        };
    }
}