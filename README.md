# Arch Scanner

[![CI](https://github.com/archguard/scanner/actions/workflows/ci.yaml/badge.svg)](https://github.com/archguard/scanner/actions/workflows/ci.yaml)

scan

* scan_git - Git commit history scan
* scan_jacoco - Jacoco scan
* scan_javasource - Java source code level scan
* scan_typescript - TypeScript source code level scan
    * [x] React support
    * [ ] Angular support
* scan_mysql
    * [ ] TableName support

todo:

- [x] source code with chapi
    - [x] Java
    - [x] TypeScript
- [ ] bytecode scan
- [x] api scan
    - [ ] Kotlin Spring
    - [ ] Java Spring
    - [ ] Frontend
        - [x] axios
        - [x] umi-request

## Todo

tech debt

- [ ] tech debt
    - [ ] better README.md
    - [x] maven to gradle
    - [ ] tests
- [x] Code scanner
    - add Chapi parser for Java, TypeScript, Kotli
- [x] continuous delivery
    - [x] GitHub workflow
    - [x] git tag publish to GitHub
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

@2020~ Thoughtworks. This code is distributed under the MPL license. See `LICENSE` in this directory.
