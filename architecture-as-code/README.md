# Architecture as Code

[![Java CI](https://github.com/archguard/architecture-as-code/actions/workflows/ci.yaml/badge.svg)](https://github.com/archguard/architecture-as-code/actions/workflows/ci.yaml)
[![codecov](https://codecov.io/gh/archguard/architecture-as-code/branch/master/graph/badge.svg?token=RSAOWTRFMT)](https://codecov.io/gh/archguard/architecture-as-code)

> 架构即代码，是一种架构设计和治理的思想，它围绕于架构的一系列模式，将架构元素、特征进行组合与呈现，并将架构决策与设计原则等紧密的与系统相结合。

core concepts see in 《[架构即代码：编码下一代企业（应用）架构体系](https://www.phodal.com/blog/architecture-as-code/)》

## Todo

DSL design:

- [ ] ArchGuard backend DSL。基于 ArchGuard Backend，提供 CRUD 封装的 API，如构建系统，查询依赖关系等。
- [ ] Architecture DSL。设计系统架构，可视化架构设计等，生成系统的架构 DSL。
- [ ] Scanner。结合 ArchGuard Scanner 中的能力，对系统进行 Scanner、Analyser、Linter 等。

REPL API:

- [ ] REPL Server

CI：

- [ ] CI

Archdoc Execute

- [ ] markdown

License
---

@2022~ ArchGuard. This code is distributed under the MIT license. See `LICENSE` in this directory.
