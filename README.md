# Arch Scanner

[![CI](https://github.com/archguard/scanner/actions/workflows/ci.yaml/badge.svg)](https://github.com/archguard/scanner/actions/workflows/ci.yaml)
[![codecov](https://codecov.io/gh/archguard/scanner/branch/master/graph/badge.svg?token=RSAOWTRFMT)](https://codecov.io/gh/archguard/scanner)
![GitHub release (latest by date)](https://img.shields.io/github/v/release/archguard/scanner)

Requirements: JDK 12

Scanner:

* scan_git - Git commit history scan
* scan_jacoco - Jacoco scan
* scan_bytecode - for JVM languages
  * known issues: Bytecode not support for API analysis.
* scan_sourcecode - Code analysis
  * SQL Analyser
  * Http Api Analyser
  * Database Analyser
* scan_test_badsmell - Test code badsmell 
    * [x] Java (based on [https://github.com/phodal/chapi-tbs](https://github.com/phodal/chapi-tbs))
* code_repository - share repository code for `scan_bytecode` and `scan_sourcecode`, can be for such as scan_llvm
  * [x] ContainerRepository
  * [x] CodeRepository
* diff_change - diff change between commits
* collector_ci [TBD] - collector CI/CD information

License
---

scan_bytecode inspired by [https://github.com/fesh0r/fernflower](https://github.com/fesh0r/fernflower)

`languages.json` based on [https://github.com/boyter/scc](https://github.com/boyter/scc) with MIT LICENSE.

@2020~2022 Thoughtworks. This code is distributed under the MPL license. See `LICENSE` in this directory.
