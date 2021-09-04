package cn.aixuxi.ossp.search.server;

import cn.aixuxi.ossp.search.server.admin.properties.IndexProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients
@EnableConfigurationProperties(IndexProperties.class)
@SpringBootApplication
public class SearchServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchServerApplication.class, args);
	}

}
