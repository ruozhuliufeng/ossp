package cn.aixuxi.ossp.transaction.tm;

import com.codingapi.txlcn.tm.config.EnableTransactionManagerServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@EnableTransactionManagerServer
@SpringBootApplication
public class OsspTxlcnTmApplication {

    public static void main(String[] args) {
        SpringApplication.run(OsspTxlcnTmApplication.class, args);
    }

}
