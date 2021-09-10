package cn.aixuxi.ossp.gateway.config;

import feign.codec.Decoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Feign配置类，配置Feign的Decoder解决在Gateway中使用Feign时报错找不到HttpMessageConvertes
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-10 14:39
 **/
@Configuration
public class FeignConfig {

    @Bean
    public Decoder feignDecoder(){
        return new ResponseEntityDecoder(new SpringDecoder(feignHttpMessageConverter()));
    }

    public ObjectFactory<HttpMessageConverters> feignHttpMessageConverter(){
        final HttpMessageConverters http = new HttpMessageConverters(new GateWayMappingJackson2HttpMessageConverter());
        return () -> http;
    }


    public static class GateWayMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter{
        GateWayMappingJackson2HttpMessageConverter(){
            List<MediaType> mediaTypes = new ArrayList<>();
            mediaTypes.add(MediaType.valueOf(MediaType.TEXT_HTML_VALUE+";charset=UTF-8"));
            setSupportedMediaTypes(mediaTypes);
        }
    }
}
