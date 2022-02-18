# ArchGuard backend

## 项目简介

ArchGuard backend services;

## 技术选型

- 开发语言：Kotlin  
- 开发框架：Spring Boot，JDBI  
- 测试框架：Junit5，Spring Boot Test，Flyway， H2  
- 构建工具：gradle  
- 数据库：MySQL  

## 本地构建

#### build
`./gradlew build`

#### run
`./gradlew bootrun`

#### docker

## 领域模型

## Flyway

### 查看当前flyway状态

1. ./gradlew clean compile
2. ./gradlew -Dflyway.configFiles=flyway.conf flywayInfo

### 校验当前flyway状态

1. ./gradlew clean compile
2. ./gradlew -Dflyway.configFiles=flyway.conf flywayValidate

### 迁移flyway

1. ./gradlew clean compile
2. ./gradlew -Dflyway.configFiles=flyway.conf flywayMigrate

### 修复flyway

1. ./gradlew clean compile
2. ./gradlew -Dflyway.configFiles=flyway.conf flywayRepair

**如果想要迁移local环境的数据库，将flyway.conf改为flyway-local.conf**
