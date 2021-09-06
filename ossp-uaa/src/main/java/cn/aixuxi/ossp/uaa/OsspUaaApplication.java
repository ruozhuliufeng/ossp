package cn.aixuxi.ossp.uaa;

import cn.aixuxi.ossp.ribbon.annotation.EnableFeignInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableFeignClients
@EnableFeignInterceptor
@EnableRedisHttpSession
@EnableDiscoveryClient
@SpringBootApplication
public class OsspUaaApplication {

    public static void main(String[] args) {
        SpringApplication.run(OsspUaaApplication.class, args);
    }

}
