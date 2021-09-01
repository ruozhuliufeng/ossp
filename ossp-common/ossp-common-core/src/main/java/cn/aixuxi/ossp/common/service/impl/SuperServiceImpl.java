package cn.aixuxi.ossp.common.service.impl;

import cn.aixuxi.ossp.common.exception.IdempotencyException;
import cn.aixuxi.ossp.common.exception.LockException;
import cn.aixuxi.ossp.common.lock.DistributedLock;
import cn.aixuxi.ossp.common.lock.OsspLock;
import cn.aixuxi.ossp.common.service.ISuperService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Service实现父类
 * @author ruozhulifeng
 * @date 2021-09-01
 */
public class SuperServiceImpl<M extends BaseMapper<T>,T>
                            extends ServiceImpl<M,T>
                            implements ISuperService<T>{
    /**
     * 幂等性新增记录
     * 例子如下：
     * String usernaem = sysUser.getUsername();
     * boolean result = super.saveIdempotency(sysUser,lock,
     * LOCKE_KEY_USERNAME+username,
     * new QueryWrapper<SysUser>().eq("username",username));
     *
     * @param entiy        实体对象
     * @param locker       锁实例
     * @param lockKey      锁的key
     * @param countWrapper 判断是否存在的条件
     * @param message      对象已存在提示信息
     * @return boolean
     * @throws Exception
     */
    @Override
    public boolean saveIdempotency(T entiy, DistributedLock locker, String lockKey, Wrapper<T> countWrapper, String message) throws Exception {
        if (locker == null){
            throw new LockException("分布式锁为空");
        }
        if (StrUtil.isEmpty(lockKey)){
            throw new LockException("锁的值为空");
        }
        try(OsspLock lock = locker.tryLock(lockKey,10,60, TimeUnit.SECONDS);) {
            if (lock != null){
                // 判断记录是否已存在
                int count = super.count(countWrapper);
                if (count == 0){
                    return super.save(entiy);
                }else {
                    if (StrUtil.isEmpty(message)){
                        message = "已存在";
                    }
                    throw new IdempotencyException(message);
                }
            }else {
                throw new LockException("锁等待超时");
            }
        }
    }

    @Override
    public boolean saveIdempotency(T entiy, DistributedLock locker, String lockKey, Wrapper<T> countWrapper) throws Exception {
        return saveIdempotency(entiy,locker,lockKey,countWrapper,null);
    }

    /**
     * 幂等性新增或更新记录
     * 例子如下：
     * String usernaem = sysUser.getUsername();
     * boolean result = super.saveOrUpdateIdempotency(sysUser,lock,
     * LOCKE_KEY_USERNAME+username,
     * new QueryWrapper<SysUser>().eq("username",username));
     *
     * @param entiy        实体对象
     * @param locker       锁实例
     * @param lockKey      锁的key
     * @param countWrapper 判断是否存在的条件
     * @param message      对象已存在提示信息
     * @return boolean
     * @throws Exception
     */
    @Override
    public boolean saveOrUpdateIdempotency(T entiy, DistributedLock locker, String lockKey, Wrapper<T> countWrapper, String message) throws Exception {
        if (null != entiy){
            Class<?> cls = entiy.getClass();
            TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
            if (null != tableInfo && StrUtil.isNotEmpty(tableInfo.getKeyProperty())){
                Object idVal = ReflectionKit.getFieldValue(entiy,tableInfo.getKeyProperty());
                if (StringUtils.checkValNull(idVal) || Objects.isNull(getById((Serializable) idVal))){
                    if (StrUtil.isEmpty(message)){
                        message = "已存在";
                    }
                    return this.saveIdempotency(entiy,locker,lockKey,countWrapper,message);
                }else {
                    return updateById(entiy);
                }
            }else {
                throw ExceptionUtils.mpe("错误：无法执行语句，找不到@TableId标记的主键！");
            }
        }
        return false;
    }

    @Override
    public boolean saveOrUpdateIdempotency(T entiy, DistributedLock locker, String lockKey, Wrapper<T> countWrapper) throws Exception {
        return saveOrUpdateIdempotency(entiy,locker,lockKey,countWrapper,null);
    }
}
