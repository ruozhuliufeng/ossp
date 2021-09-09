package cn.aixuxi.ossp.business.code;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class OsspCodeGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(OsspCodeGeneratorApplication.class, args);
    }

}
