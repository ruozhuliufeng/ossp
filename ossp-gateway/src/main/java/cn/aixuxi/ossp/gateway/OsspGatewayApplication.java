package cn.aixuxi.ossp.gateway;

import cn.aixuxi.ossp.common.lb.annotation.EnableBaseFeignInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "cn.aixuxi.ossp")
@EnableBaseFeignInterceptor
@EnableDiscoveryClient
@SpringBootApplication
public class OsspGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(OsspGatewayApplication.class, args);
    }

}
