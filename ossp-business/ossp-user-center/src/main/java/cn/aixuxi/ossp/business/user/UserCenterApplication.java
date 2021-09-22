package cn.aixuxi.ossp.business.user;

import cn.aixuxi.ossp.common.lb.annotation.EnableFeignInterceptor;
import cn.aixuxi.ossp.search.client.annotation.EnableSearchClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableDiscoveryClient
@EnableSearchClient
@EnableTransactionManagement
@EnableFeignInterceptor
@SpringBootApplication
public class UserCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserCenterApplication.class, args);
    }

}
