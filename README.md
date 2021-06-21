# OSSP - 一站式服务平台
    One Stop Service Platform(一站式服务平台)，
## 什么是OSSP

## 解决了什么问题

## 技术选型

## 系统架构

## 模块说明

```lua
ossp -- 父项目，公共依赖管理
│  ├─ossp-common -- 通用工具模块
│  ├─doc -- 项目相关文档
│  ├─sql -- 项目sql语句
│  ├─ossp-gateway -- api网关工程(spring-cloud-gateway)[8088]
│  ├─ossp-job -- 分布式任务调度一级工程
│  │  ├─job-admin -- 任务管理器[8900]
│  │  ├─job-core -- 任务调度核心代码
│  │  ├─job-executor-samples -- 任务执行者executor样例[8902]
│  ├─ossp-monitor -- 监控一级工程
│  │  ├─ossp-admin -- 应用监控[6500]
│  │  ├─log-center -- 日志中心[7200]
│  ├─ossp-uaa -- spring-security认证中心[8000]
│  ├─ossp-web -- 前端工程[8888]
│  ├─ossp-transaction -- 事务工程
│  ├─ossp-demo -- demo一级工程
│  │  ├─seata-demo -- seata分布式事务demo
│  │  ├─rocketmq-demo -- rocketmq和mq事务demo
│  │  ├─sso-demo -- 单点登录demo
```

## 界面预览


