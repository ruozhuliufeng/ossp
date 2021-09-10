package cn.aixuxi.ossp.gateway.route;

import cn.aixuxi.ossp.common.utils.JsonUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * Nacos路由数据源
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-10 13:36
 **/
@Slf4j
public class NacosRouteDefinitionRepository implements RouteDefinitionRepository {

    private static final String SCG_DATA_ID = "scg-routes";
    private static final String SCG_GROUP_ID = "SCG_GATEWAY";

    private ApplicationEventPublisher publisher;
    private NacosConfigProperties nacosConfigProperties;

    public NacosRouteDefinitionRepository(ApplicationEventPublisher publisher,
                                          NacosConfigProperties nacosConfigProperties){
        this.publisher = publisher;
        this.nacosConfigProperties = nacosConfigProperties;
        addListener();
    }
    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        try {
            String content = nacosConfigProperties.configServiceInstance().getConfig(SCG_DATA_ID,SCG_GROUP_ID,5000);
            List<RouteDefinition> routeDefinitions = getListByStr(content);
            return Flux.fromIterable(routeDefinitions);
        }catch (NacosException e){
            log.error("获取Nacos路由失败！失败原因：{}",e.getMessage());
        }
        return Flux.fromIterable(CollUtil.newArrayList());
    }

    /**
     * 添加Nacos监听
     */
    private void addListener(){
        try{
            nacosConfigProperties.configServiceInstance().addListener(SCG_DATA_ID, SCG_GROUP_ID, new Listener() {
                @Override
                public Executor getExecutor() {
                    return null;
                }

                @Override
                public void receiveConfigInfo(String s) {
                    publisher.publishEvent(new RefreshRoutesEvent(this));
                }
            });
        }catch (NacosException e){
            log.error("Nacos监听添加失败！失败原因：{}",e.getMessage());
        }
    }

    private List<RouteDefinition> getListByStr(String content){
        if (StrUtil.isNotEmpty(content)){
            return JsonUtil.toList(content,RouteDefinition.class);
        }
        return new ArrayList<>(0);
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return null;
    }
}
