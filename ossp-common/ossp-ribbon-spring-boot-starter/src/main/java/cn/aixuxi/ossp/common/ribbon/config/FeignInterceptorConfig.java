package cn.aixuxi.ossp.common.ribbon.config;

import cn.aixuxi.ossp.common.constant.SecurityConstants;
import cn.aixuxi.ossp.common.context.TenantContextHolder;
import cn.hutool.core.util.StrUtil;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

/**
 * feign拦截器，只包含基础数据
 * @author ruozhuliufeng
 * @date 2021-09-02
 */
public class FeignInterceptorConfig {

    /**
     * 使用feign client访问别的微服务时，将上游传过来的client等信息放入header，传递给下一个微服务
     * @return
     */
    @Bean
    public RequestInterceptor baseFeignInterceptor(){
        return template -> {
            // 传递client
            String tenant = TenantContextHolder.getTenant();
            if (StrUtil.isNotEmpty(tenant)){
                template.header(SecurityConstants.TENANT_HEADER,tenant);
            }
        };
    }
}
