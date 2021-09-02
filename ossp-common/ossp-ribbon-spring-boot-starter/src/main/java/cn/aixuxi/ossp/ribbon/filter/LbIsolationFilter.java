package cn.aixuxi.ossp.ribbon.filter;

import cn.aixuxi.ossp.common.constant.CommonConstant;
import cn.aixuxi.ossp.common.constant.ConfigConstants;
import cn.aixuxi.ossp.common.context.LbIsolationContextHolder;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Filter;

/**
 * 负载均衡隔离规则过滤器
 * @author ruozhuliufeng
 * @date 2021-09-02
 */
@ConditionalOnClass(Filter.class)
public class LbIsolationFilter extends OncePerRequestFilter {

    @Value("${"+ ConfigConstants.CONFIG_RIBBON_ISOLATION_ENABLED +":false}")
    private boolean enableIsolation;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !enableIsolation;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String version = request.getHeader(CommonConstant.O_S_S_P_VERSION);
            if (StrUtil.isNotEmpty(version)){
                LbIsolationContextHolder.setVersion(version);
            }
            filterChain.doFilter(request,response);
        }finally {
            LbIsolationContextHolder.clear();
        }
    }
}
