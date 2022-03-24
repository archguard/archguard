# ArchGuard backend

[![codecov](https://codecov.io/gh/archguard/archguard-backend/branch/master/graph/badge.svg?token=QS5H866CWH)](https://codecov.io/gh/archguard/archguard-backend)
![GitHub release (latest by date)](https://img.shields.io/github/v/release/archguard/archguard-backend)

Features

- C4 analysis
    - [ ] context dependency analysis.
    - [ ] container dependency analysis. (level: HTTP API)
    - [x] component (module) dependency analysis.
    - [x] code dependency analysis. (level: package, class, method)
- Scanner integration
    - [x] PMD
    - [x] Git with jGit
        - [x] HotFile
    - [x] Java/Jvm only
        - [ ] JVM Bytecode (need to rewrite with License issue)
        - [x] CheckStyle
        - [x] Badsmell by DesigniteJava
        - [x] Test Badsmell by Coca (Java only)
    - [x] TypeScript with Chapi
    - [x] Kotlin with Chapi
    - [x] Git Hot File 
- System Info
    - [ ] Custom build command  

## Development

todo:

- [ ] tech debt
    - [ ] tech stack upgrading
    - [ ] InfluxDB to 2.0
    - [ ] clean unused code
    - [ ] database lint/checkstyle
    - [ ] api lint
    - [ ] kotlin lint
- [ ] scanner
    - [x] download from GitHub by config
    - [ ] enable scanner failure
    - [x] config scanner by optional
- [ ] System landscape by C4
    - [ ] Context = System (props: name, aliasName...)
    - [ ] Containers = Repository or repository with modules (props: name, path, repository...)
    - [ ] Components = Module (props: name, path, repository...)
    - [ ] Code = Code dependence (props: name...)
- [ ] User Experience Improve
    - [ ] custom build command for SystemInfo
    - [ ] download scanner to local

test projects:

- [https://github.com/domain-driven-design/ddd-lite-example](https://github.com/domain-driven-design/ddd-lite-example)
- [https://github.com/domain-driven-design/ddd-monolithic-code-sample](https://github.com/domain-driven-design/ddd-monolithic-code-sample)

### 技术选型

- 开发语言：Kotlin
- 开发框架：Spring Boot，JDBI
- 测试框架：Junit5，Spring Boot Test，Flyway，H2
- 构建工具：Gradle
- 数据库：MySQL, InfluxDB

### 本地构建

1. create database: `create database archguard default character set utf8mb4 collate utf8mb4_unicode_ci;`

2run: `./gradlew bootrun`

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

### Docker

#### user `docker-compose`

```
docker-compose up
```

#### 仅启动后端

1. 启动本地数据库

```
docker run --name mysql-archguard -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=archguard -p 3306:3306 -d mysql:8
```

2. 指定`application-local.properties`启动Application

### InfluxDB

```
brew install influxdb@1
```

License
---

@2020~2022 Thoughtworks. This code is distributed under the MPL license. See `LICENSE` in this directory.
