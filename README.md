# code-analysis-addition

## 项目简介
此项目包含code-analysis-backend以外的后台接口。

## 技术选型
开发语言：Kotlin  
开发框架：Spring Boot，JDBI  
测试框架：Junit5，Spring Boot Test，Flyway， H2  
构建工具：gradle  
数据库：MySQL  

## 本地构建
#### build
`./gradlew build`

#### run
`./gradlew bootrun`

#### docker

## 领域模型

## 测试策略
#### 分类
+ API测试
    进行API级别测试
+ 单元测试
    对各层业务处理进行单元测试
+ 人工测试

#### 策略
+  非必要测试
    对于无业务逻辑，只做数据转发的层级不做单元测试；对于简单Sql，不做单元测试
+ 必要测试
    含有复杂业务逻辑，必须进行单元和API测试；对于复杂Sql，必须做单元测试

## 技术架构图

## 部署架构图

## 外部依赖

## 环境信息

## 编码实践

## FAQ
