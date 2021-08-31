package cn.aixuxi.ossp.common.zookeeper.lock;

import cn.aixuxi.ossp.common.constant.CommonConstant;
import cn.aixuxi.ossp.common.lock.DistributedLock;
import cn.aixuxi.ossp.common.lock.OsspLock;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.CommonDataSource;
import java.util.concurrent.TimeUnit;

/**
 * Zookeeper分布式锁实现
 *
 * @author ruozhuliufeng
 * @date 2021-08-31
 */
@Component
@ConditionalOnProperty(prefix = "ossp.lock", name = "lockerType", havingValue = "ZK")
public class ZookeeperDistributedLock implements DistributedLock {

    @Resource
    private CuratorFramework client;


    private OsspLock getLock(String key){
        InterProcessMutex lock = new InterProcessMutex(client,getPath(key));
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
        OsspLock osspLock = this.getLock(key);
        InterProcessMutex ipm = (InterProcessMutex) osspLock.getLock();
        ipm.acquire();
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
        OsspLock osspLock = this.getLock(key);
        InterProcessMutex ipm = (InterProcessMutex) osspLock.getLock();
        if (ipm.acquire(waitTime,unit)){
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
        if (lock != null){
            if (lock instanceof InterProcessMutex){
                InterProcessMutex ipm = (InterProcessMutex) lock;
                if (ipm.isAcquiredInThisProcess()){
                    ipm.release();
                }
            }else {
                throw new LockedException("锁类型需要为InterProcessMutex，释放失败！");
            }
        }
    }

    private String getPath(String key) {
        return CommonConstant.PATH_SPLIT + CommonConstant.LOCK_KEY_PREFIX + CommonConstant.PATH_SPLIT + key;
    }
}
