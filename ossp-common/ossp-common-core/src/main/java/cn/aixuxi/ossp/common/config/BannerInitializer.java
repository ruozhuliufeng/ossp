package cn.aixuxi.ossp.common.config;

import cn.aixuxi.ossp.common.constant.CommonConstant;
import cn.aixuxi.ossp.common.utils.CustomBanner;
import com.nepxion.banner.BannerConstant;
import com.nepxion.banner.Description;
import com.nepxion.banner.LogoBanner;
import com.taobao.text.Color;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Spring Boot Banner 初始化
 * @author ruozhuliufeng
 * @date 2021-07-17
 */
public class BannerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        if (!(configurableApplicationContext instanceof AnnotationConfigApplicationContext)){
            LogoBanner logoBanner = new LogoBanner(BannerInitializer.class,"ossp/logo.txt","Welcome to OSSP",5,6,new Color[5],true);
            CustomBanner.show(logoBanner,new Description(BannerConstant.VERSION + ":", CommonConstant.PROJECT_VERSION,0,1),
                    new Description("Github:","https://github.com/ruozhuliufeng",0,1),
                    new Description("Blog","https://csdn.net/ruozhuliufeng",0,1));
        }
    }
}
