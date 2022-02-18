# Arch Scanner 

[![CI](https://github.com/archguard/scanner/actions/workflows/ci.yaml/badge.svg)](https://github.com/archguard/scanner/actions/workflows/ci.yaml)

* scan_git - Git 提交记录扫描
* scan_jacoco Jacoco 结果扫描

## Todo

tech debt

- [ ] tech debt
  - [ ] better README.md
  - [x] maven to gradle
  - [ ] core code model
  - [ ] tests
- [ ] Java
  - add Chapi parser
  - antlr grammar: [https://github.com/antlr/grammars-v4/tree/master/java/java](https://github.com/antlr/grammars-v4/tree/master/java/java)
- [ ] continuous delivery
  - [x] GitHub workflow 
  - [ ] git tag publish to GitHub
  - [ ] git tag publish to mavne

model align

- [ ] model
  - [ ] code model with full functions
  - [ ] save model in local
- [ ] data pipeline 
  - data feeder to database/json/others
  - data transform function

addition futures: 

- [ ] languages scanner
  - [ ] TypeScript
- [ ] cloc scanner

## Usage

### scan_git 工具 

说明：`程序会扫描指定的 GIT 仓库， 并在命令当前目录下生成SQL文件 output.sql`

运行：

```
 ./gradlew run --gitPath=. --branch=master
```

License
---

@ 2020~ Thoughtworks.  This code is distributed under the MPL license. See `LICENSE` in this directory.
