# 统一登录认证中心

## 依赖介绍
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <!-- 统一版本依赖 -->
    <parent>
        <artifactId>ossp</artifactId>
        <groupId>cn.aixuxi</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <groupId>cn.aixuxi.ossp</groupId>
    <artifactId>ossp-uaa</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>ossp-uaa</name>
    <description>OSSP 用户认证与授权服务</description>
    <properties>
        <java.version>1.8</java.version>
    </properties>
    <dependencies>
        <!-- 引入通用工具依赖 -->
        <dependency>
            <groupId>cn.aixuxi.ossp</groupId>
            <artifactId>ossp-common</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
        <!-- Web核心 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!-- 生成验证码 -->
        <dependency>
            <groupId>com.github.axet</groupId>
            <artifactId>kaptcha</artifactId>
            <version>0.0.9</version>
        </dependency>
        <!-- oauth2依赖 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-oauth2</artifactId>
        </dependency>
        <!-- Cloud Security -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-security</artifactId>
        </dependency>
        <!-- JWT -->
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-jwt</artifactId>
            <version>1.0.10.RELEASE</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>

```

## 配置文件介绍
```yaml
server:
  port: 8888
spring:
  application:
    # 必须，用于在Nacos服务中区分不同的微服务
    name: ossp-uaa
  datasource:
    # 数据库连接池
    druid:
      url: jdbc:mysql://localhost:3306/security-auth?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false
      username: root
      password: root
      driver-class-name: com.mysql.jdbc.Driver
  cloud:
    nacos:
      discovery:
        # Nacos的服务地址
        server-addr: 114.115.167.133:8848
  redis:
    database: 0
    host: localhost
    port: 6379
    password: ''
mybatis-plus:
  mapper-locations: classpath:mapper/*.xml
```