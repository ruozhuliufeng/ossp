package cn.aixuxi.ossp.gateway.filter;

import cn.aixuxi.ossp.common.log.monitor.PointUtil;
import cn.aixuxi.ossp.gateway.utils.ReactiveAddrUtil;
import cn.hutool.core.util.StrUtil;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 请求统计分析埋点过滤器
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-10 15:25
 **/
@Component
public class RequestStatisticsFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        Map<String,String> headers = request.getHeaders().toSingleValueMap();
        UserAgent userAgent = UserAgent.parseUserAgentString(headers.get("User-Agent"));
        // 埋点
        PointUtil.debug("1","request-statistics",
                "ip="+ ReactiveAddrUtil.getRemoteAddr(request)
                    + "&browser="+getBrowser(userAgent.getBrowser().getName())
                    + "&operatingSystem=" + getOperatingSystem(userAgent.getOperatingSystem().getName()));
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
        return 0;
    }

    private String getBrowser(String browser){
        if (StrUtil.isNotEmpty(browser)){
            if (browser.contains("CHROME")){
                return "CHROME";
            }else if (browser.contains("FIREFOX")){
                return "FIREFOX";
            }else if (browser.contains("SAFARI")){
                return "SAFARI";
            }else if (browser.contains("EDGE")){
                return "EDGE";
            }
        }
        return browser;
    }

    private String getOperatingSystem(String operatingSystem){
        if (StrUtil.isNotEmpty(operatingSystem)){
            if (operatingSystem.contains("MAC_OS_X")){
                return "MAC_OS_X";
            }else if (operatingSystem.contains("ANDROID")){
                return "ANDROID";
            }
        }
        return operatingSystem;
    }
}
