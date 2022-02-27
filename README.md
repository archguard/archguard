# ArchGuard backend

Features

## Development

- [API List](http://localhost:8080/api/v3/api-docs)

todo:

- [ ] tech debt
   - [ ] tech stack upgrading
   - [ ] InfluxDB to 2.0
   - [ ] clean unused code
   - [ ] database lint/checkstyle
   - [ ] api lint
   - [ ] kotlin lint
- [ ] scanner
   - [ ] download from github by config
   - [ ] enable scanner failure
   - [ ] config scanner by optional
- [ ] System landscape by C4
   - [ ] Context = System (props: name, aliasName...)
   - [ ] Containers = Repository or repository with modules (props: name, path, repository...)
   - [ ] Components = Module (props: name, path, repository...)
   - [ ] Code = Code dependence (props: name...)

### Docker

Docker Image 地址

```
docker pull archguard/archguard-backend:latest
docker pull archguard/archguard-backend:vx.x.x
```

本地 docker compose 部署

```
docker-compose up
```

### 技术选型

- 开发语言：Kotlin  
- 开发框架：Spring Boot，JDBI  
- 测试框架：Junit5，Spring Boot Test，Flyway， H2  
- 构建工具：gradle  
- 数据库：MySQL, InfluxDB

### 本地构建

build:

`./gradlew build`

run:

`./gradlew bootrun`

### Flyway

#### 查看当前flyway状态

1. ./gradlew clean compile
2. ./gradlew -Dflyway.configFiles=flyway.conf flywayInfo

#### 校验当前flyway状态

1. ./gradlew clean compile
2. ./gradlew -Dflyway.configFiles=flyway.conf flywayValidate

#### 迁移flyway

1. ./gradlew clean compile
2. ./gradlew -Dflyway.configFiles=flyway.conf flywayMigrate

#### 修复flyway

1. ./gradlew clean compile
2. ./gradlew -Dflyway.configFiles=flyway.conf flywayRepair

**如果想要迁移local环境的数据库，将flyway.conf改为flyway-local.conf**

### macOS setup

### InfluxDB

```
brew install influxdb@1
```

License
---

@ 2020 ~ Thoughtworks. This code is distributed under the MPL license. See `LICENSE` in this directory.



