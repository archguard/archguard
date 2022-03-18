# Arch Scanner

[![CI](https://github.com/archguard/scanner/actions/workflows/ci.yaml/badge.svg)](https://github.com/archguard/scanner/actions/workflows/ci.yaml)
[![codecov](https://codecov.io/gh/archguard/scanner/branch/master/graph/badge.svg?token=RSAOWTRFMT)](https://codecov.io/gh/archguard/scanner)
![GitHub release (latest by date)](https://img.shields.io/github/v/release/archguard/scanner)


scan

* scan_git - Git commit history scan
* scan_jacoco - Jacoco scan
* scan_sourcecode - Java source code level scan
    * [ ] TypeScript
        * [x] React support
        * [ ] Angular support
        * [ ] Vue support
        * [ ] HTTP API
            * [x] axios
            * [x] umi-request
* scan_mysql
    * [ ] TableName support
* scan_test_badsmell
    * [x] based on [https://github.com/phodal/chapi-tbs](https://github.com/phodal/chapi-tbs)

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

## scan_sourcecode

```
java "-Ddburl=jdbc:mysql://localhost:3306/archguard?user=root&password=&useSSL=false" -jar scan_sourcecode-1.1.7-all.jar --system-id=8 --path=scan_git --language=kotlin
```

License
---

@2020~ Thoughtworks. This code is distributed under the MPL license. See `LICENSE` in this directory.
