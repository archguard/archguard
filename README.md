# Arch Scanner

[![CI](https://github.com/archguard/scanner/actions/workflows/ci.yaml/badge.svg)](https://github.com/archguard/scanner/actions/workflows/ci.yaml)
[![codecov](https://codecov.io/gh/archguard/scanner/branch/master/graph/badge.svg?token=RSAOWTRFMT)](https://codecov.io/gh/archguard/scanner)
![GitHub release (latest by date)](https://img.shields.io/github/v/release/archguard/scanner)

Requirements: JDK 12

Scanner:

* diff_change - diff change between commits
* scan_git - Git commit history scan
* analyser_architecture - analysis architecture
* scan_sourcecode - Code analysis
  * scanner_sourcecode
       * feat_apicalls
       * feat_datamap
       * lang_csharp
       * lang_golang
       * lang_java
       * lang_kotlin
       * lang_python
       * lang_scala
       * lang_typescript
  * scanner_cli
  * scanner_core
* linter
  * rule_code
  * rule_sql
  * rule_test_code
  * rule_webapi
* rule_core

## Inspires

ArchGuard Scanner is inspired by a lot of projects.

- scan_bytecode inspired by [https://github.com/fesh0r/fernflower](https://github.com/fesh0r/fernflower)
- linter rule system inspired by [https://github.com/pinterest/ktlint](https://github.com/pinterest/ktlint)
- CLOC inspired by [https://github.com/boyter/scc](https://github.com/boyter/scc), and `languages.json` based on SCC with MIT LICENSE.

License
---

`languages.json` based on [https://github.com/boyter/scc](https://github.com/boyter/scc) with MIT LICENSE.

@2020~2022 Thoughtworks. This code is distributed under the MPL license. See `LICENSE` in this directory.
