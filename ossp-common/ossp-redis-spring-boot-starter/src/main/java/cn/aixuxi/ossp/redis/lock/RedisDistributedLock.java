package cn.aixuxi.ossp.redis.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Redis实现分布式锁
 * 注解注释：@Deprecated:标记为过时的技术
 *         建议使用Redisson的实现方式{@link RedissonDistributedLock}
 * @author ruozhuliufeng
 * @date 2021-07-19
 */
@Slf4j
@ConditionalOnClass(RedisTemplate.class)
@Deprecated
public class RedisDistributedLock {
}
