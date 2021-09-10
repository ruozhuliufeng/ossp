package cn.aixuxi.ossp.gateway.swagger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Swagger处理
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-10 15:00
 **/
@RestController
@RequestMapping("/swagger-resources")
public class SwaggerHandler {
    private final SwaggerResourcesProvider swaggerResources;
    @Autowired(required = false)
    private SecurityConfiguration securityConfiguration;
    @Autowired(required = false)
    private UiConfiguration uiConfiguration;

    public SwaggerHandler(SwaggerResourcesProvider swaggerResources){
        this.swaggerResources = swaggerResources;
    }

    @GetMapping("/configuration/security")
    public Mono<ResponseEntity<SecurityConfiguration>> securityConfiguration(){
        return Mono.just(new ResponseEntity<>(
                Optional.ofNullable(securityConfiguration)
                        .orElse(SecurityConfigurationBuilder.builder().build()),
                HttpStatus.OK
        ));
    }
    @GetMapping("/configuration/ui")
    public Mono<ResponseEntity<UiConfiguration>> uiConfiguration(){
        return Mono.just(new ResponseEntity<>(
           Optional.ofNullable(uiConfiguration)
                   .orElse(UiConfigurationBuilder.builder().build()),
                HttpStatus.OK
        ));
    }

    @GetMapping
    public Mono<ResponseEntity> swaggerResources(){
        return Mono.just((new ResponseEntity<>(swaggerResources.get(),HttpStatus.OK)));
    }
}
