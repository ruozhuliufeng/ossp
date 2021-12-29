package cn.aixuxi.ossp.gateway.filter;

import cn.aixuxi.ossp.common.constant.CommonConstant;
import cn.aixuxi.ossp.common.log.properties.TraceProperties;
import cn.aixuxi.ossp.common.log.trace.MDCTraceUtils;
import cn.hutool.core.util.IdUtil;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 生成日志链路追踪id，并传入header中
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-10 15:15
 **/
@Component
public class TraceFilter implements GlobalFilter, Ordered {
    @Autowired
    private TraceProperties traceProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (traceProperties.getEnable()){
            // 链路追踪id
            MDCTraceUtils.addTraceId();
            ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate()
                    .headers(h -> {
                            h.add(MDCTraceUtils.TRACE_ID_HEADER,MDCTraceUtils.getTraceId());
                            h.add(MDCTraceUtils.SPAN_ID_HEADER,MDCTraceUtils.getTraceId());
                    })
                    .build();
            ServerWebExchange build = exchange.mutate().request(serverHttpRequest).build();
            return chain.filter(build);
        }
        return chain.filter(exchange);
    }

    /**
     * Get the order value of this object.
     * <p>Higher values are interpreted as lower priority. As a consequence,
     * the object with the lowest value has the highest priority (somewhat
     * analogous to Servlet {@code load-on-startup} values).
     * <p>Same order values will result in arbitrary sort positions for the
     * affected objects.
     *
     * @return the order value
     * @see #HIGHEST_PRECEDENCE
     * @see #LOWEST_PRECEDENCE
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
