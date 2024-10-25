
Refs:

- [Architecture Description Language (ADL)](https://cio-wiki.org/wiki/Architecture_Description_Language_(ADL))

## 通过领域生成架构特征

1. 如何识别领域知识？

## 多个子系统导入

基于 CSV, JSON， Markdown

## 基于依赖的框架映射，分析出架构分格

Maven Index: [https://maven.apache.org/repository/central-index.html](https://maven.apache.org/repository/central-index.html)

示例：

1. Spring Boot => 微服务架构
2. Equinox => OSGI
3. Flink, Kafka => Data => ServiceBased

## Arch

```yaml
# 分析态输出
- langauge: java
  conceptualArch:
    domains: [ "" ]
    styles:
      layeredStyle: [""]
  moduleArch:
    systems: [ "" ]
    subSystems: [ "" ]
    # from Gradle or maven
    modules: [ "" ]
    layers: [ "" ]
    interfaces: [ "" ]
    changeImpact: [ "" ]
  executionArch:
    # Interface description language
    interfaceDescriptionLanguage: [ "proto" ]
    messageQueue: [ "RabbitMQ", "Kafka" ]
    # hasProcesses
    processes: [ "ProcessBuilder" ]
    # thread
    # kotlin.concurrent.thread
    servers: [ "" ]
  codeArch:
    # from CLOC
    languages: [ "" ]
    # packageManager
    developmentTools: [ "" ]
    libraries: [ "" ]
    packages: [ "" ]
    directories: [ "" ]
    files: [ "" ]
```

## 案例库

```yaml
# Domain
- domain: GUI
  characteristics:
    - WindowManager
- domain: finance
  characteristics:
    - 并发
```