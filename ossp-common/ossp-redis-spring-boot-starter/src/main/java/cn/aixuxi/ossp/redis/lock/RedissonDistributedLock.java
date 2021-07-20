package cn.aixuxi.ossp.redis.lock;

import cn.aixuxi.ossp.common.constant.CommonConstant;
import cn.aixuxi.ossp.common.lock.DistributedLock;
import cn.aixuxi.ossp.common.lock.OsspLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.authentication.LockedException;

import java.util.concurrent.TimeUnit;

/**
 * Redisson实现分布式锁,基本锁功能的抽象实现，可以满足大部分的徐阿偶
 * @author ruozhuliufeng
 * @date 2021-07-20
 */
@ConditionalOnClass(RedissonClient.class)
@ConditionalOnProperty(prefix = "ossp.lock",name = "lockerType",havingValue = "REDIS",matchIfMissing = true)
public class RedissonDistributedLock implements DistributedLock {

    @Autowired
    private RedissonClient redisson;

    private OsspLock getLock(String key,boolean isFair){
        RLock lock;
        if (isFair){
            lock = redisson.getFairLock(CommonConstant.LOCK_KEY_PREFIX+":"+key);
        }else {
            lock = redisson.getLock(CommonConstant.LOCK_KEY_PREFIX+":"+key);
        }
        return new OsspLock(lock,this);
    }

    /**
     * 获取锁，如果获取不成功，则一直等待直到lock被获取
     *
     * @param key       锁的key
     * @param leaseTime 加锁的时间，超过这个时间锁便会自动解锁；
     *                  如果leaseTime为-1，则保持锁定直到显式解锁
     * @param unit      leaseTime参数的时间单位
     * @param isFair    是否为公平锁
     * @return 锁对象
     * @throws Exception 获取锁中产生的异常
     */
    @Override
    public OsspLock lock(String key, long leaseTime, TimeUnit unit, boolean isFair) throws Exception {
        OsspLock osspLock = getLock(key,isFair);
        RLock lock = (RLock) osspLock.getLock();
        lock.lock(leaseTime,unit);
        return osspLock;
    }

    /**
     * 尝试获取锁，如果锁不可用，则等待最多waitTime时间后放弃
     *
     * @param key       锁的key
     * @param waitTime  获取锁的最大尝试时间(单位unit)
     * @param leaseTime 加锁的时间，超过这个时间锁便会自动解锁；
     *                  如果leaseTime为-1，则保持锁定直到显式解锁
     * @param unit      waitTime和leaseTime参数的时间单位
     * @param isFair    是否公平锁
     * @return 锁对象，如果获取锁失败则为null
     * @throws Exception 异常
     */
    @Override
    public OsspLock tryLock(String key, long waitTime, long leaseTime, TimeUnit unit, boolean isFair) throws Exception {
        OsspLock osspLock = getLock(key,isFair);
        RLock lock = (RLock) osspLock.getLock();
        if (lock.tryLock(waitTime,leaseTime,unit)){
            return osspLock;
        }
        return null;
    }

    /**
     * 释放锁
     *
     * @param lock 锁对象
     * @throws Exception 异常
     */
    @Override
    public void unlock(Object lock) throws Exception {
        if (lock!=null){
            if (lock instanceof RLock){
                RLock rLock = (RLock) lock;
                if (rLock.isLocked()){
                    rLock.unlock();
                }
            }else{
                throw new LockedException("锁的类型有误！");
            }
        }
    }
}
