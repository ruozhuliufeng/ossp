package cn.aixuxi.ossp.ribbon.rule;

import cn.aixuxi.ossp.common.constant.CommonConstant;
import cn.aixuxi.ossp.common.context.LbIsolationContextHolder;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.RoundRobinRule;
import com.netflix.loadbalancer.Server;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 根据实例版本号隔离规则，详细用法：https://mp.weixin.qq.com/s/9XQ-SIbYsov3KBx9TGFN0g<br>
 * 实例规则获取顺序如下(不满足则走下一个规则):<br>
 * 1. 相同版本号的实例<br>
 * 2. 无版本号的实例<br>
 * 3. 所有实例中轮询<br>
 * @author ruozhuliufeng
 * @date 2021-09-02
 */
public class VersionIsolationRule extends RoundRobinRule {
    private final static String KEY_DEFAULE = "default";

    @Override
    public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null){
            return null;
        }
        String version;
        if (key != null && !KEY_DEFAULE.equals(key)){
            version = key.toString();
        }else {
            version = LbIsolationContextHolder.getVersion();
        }

        List<Server> targetList = null;
        List<Server> upList = lb.getReachableServers();
        if (StrUtil.isNotEmpty(version)){
            // 取指定版本号的实例
            targetList = upList.stream().filter(
                    server -> version.equals(
                            ((NacosServer) server).getMetadata().get(CommonConstant.METADATA_VERSION)
                    )
            ).collect(Collectors.toList());
        }
        if (CollUtil.isNotEmpty(targetList)){
            // 只取无版本号的实例
            targetList = upList.stream().filter(
                    server -> {
                        String metadataVersion = ((NacosServer) server).getMetadata().get(CommonConstant.METADATA_VERSION);
                        return StrUtil.isEmpty(metadataVersion);
                    }
            ).collect(Collectors.toList());
        }
        if (CollUtil.isNotEmpty(targetList)){
            return getServer(targetList);
        }
        return super.choose(lb,key);
    }

    /**
     * 随机取一个实例
     * @param targetList 实例列表
     * @return 实例
     */
    private Server getServer(List<Server> targetList) {
        int nextInt = RandomUtil.randomInt(targetList.size());
        return targetList.get(nextInt);
    }
}
