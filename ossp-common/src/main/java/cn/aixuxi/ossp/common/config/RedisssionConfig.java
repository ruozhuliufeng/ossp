package cn.aixuxi.ossp.common.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * 类描述 分布式锁Redission配置
 *
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021/6/23 21:24
 */
@Configuration
public class RedisssionConfig {

    /**
     * 功能描述: 所有对Redisson的使用都是通过RedissonClient对象
     * @return : org.redisson.api.RedissonClient
     * @author : ruozhuliufeng
     * @date : 2020/9/19 17:29
     */
    @Bean(destroyMethod = "shutdown")
    RedissonClient redisson() throws IOException {
        // 创建配置
        Config config = new Config();
        // redis的连接需要以redis:// 或rediss://开头
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        // 根据Config创建出RedissionClient
        return Redisson.create(config);
    }
}
