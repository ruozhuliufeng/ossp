package cn.aixuxi.ossp.common.config;

import cn.aixuxi.ossp.common.utils.CustomThreadPoolTaskExecutor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.spi.CurrencyNameProvider;

/**
 * @author ruozhuliufeng
 * @date 2021-09-01
 */
@Setter
@Getter
@EnableAsync(proxyTargetClass = true)
public class DefaultAsycTaskConfig {
    /**
     * 线程池维护线程的最小数量
     */
    @Value("${asyc-task.corePoolSize:10}")
    private int corePoolSize;
    /**
     * 线程池维护线程的最大数量
     */
    @Value("${asyc-task.maxPoolSize:200}")
    private int maxPoolSize;
    /**
     * 队列最大长度
     */
    @Value("${asyc-task.queueCapacity:10}")
    private int queueCapacity;
    /**
     * 线程池前缀
     */
    @Value("${asyc-task.threadNamePrefix:OsspExecutor-}")
    private String threadNamePrefix;

    @Bean
    public TaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor executor = new CustomThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadNamePrefix);
        /**
         * rejection-policy:当pool已经达到max size的实施，如何处理新任务
         * CALLER_RUNS:不在新线程中执行任务，而是由调用者所在的线程来执行
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
