# Arch Scanner 

[![CI](https://github.com/archguard/scanner/actions/workflows/ci.yaml/badge.svg)](https://github.com/archguard/scanner/actions/workflows/ci.yaml)

scan

* scan_git - Git 提交记录扫描
* scan_jacoco - Jacoco 结果扫描
* scan_javasource - Java 源码扫描

ident

* ident_mysql - 用于标识 MySQL 语句的 `@Query(select...)` 信息等

todo:

- [ ] source code with chapi
  - [ ] Java
  - [ ] TypeScript
- [ ] bytecode scan
- [ ] api scan
  - [ ] Java Spring
- [ ] openapi (yaml/json) insert

## Todo

tech debt

- [ ] tech debt
  - [ ] better README.md
  - [x] maven to gradle
  - [ ] tests
- [ ] Code scanner
  - add Chapi parser for Java, TypeScript, Kotlin such as...
- [ ] continuous delivery
  - [x] GitHub workflow 
  - [x] git tag publish to GitHub
  - [ ] git tag publish to Maven
- [ ] data pipeline 
  - data feeder to database/json/others
  - data transform function
- [ ] cloc scanner
- [ ] code change counts

## Usage

### scan_git 工具 

说明：`程序会扫描指定的 GIT 仓库， 并在命令当前目录下生成SQL文件 output.sql`

运行：`./gradlew :scan_git:run --args="--path=.."`

### scan_jacoco 工具

说明：`扫描目标项目下的 jacoco.exec 文件， 将目标项目的覆盖率数据生成 SQL 文件 output.sql`

运行：`./gradlew :scan_jacoco:run --args="--target-project=."`

License
---

@ 2020~ Thoughtworks.  This code is distributed under the MPL license. See `LICENSE` in this directory.
