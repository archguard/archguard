# ArchGuard ArchDoc

ArchDoc is architecture as code for ArchGuard

DSL for ArchGuard

## 系统组成

repo for source?

```kotlin
// for config repos
repos { 
    repo(name = "Backend", language = "Kotlin", scmUrl = "https://github.com/archguard/archguard")
    repo(name = "Frontend", language = "TypeScript", scmUrl = "https://github.com/archguard/archguard-frontend")
    repo(name = "Scanner", language = "Kotlin", scmUrl = "https://github.com/archguard/scanner")
}
``` 

or table ？

$archdoc$repos:start     # flag for ArchDoc

| name     | language   | scmUrl                                          |
|----------|------------|-------------------------------------------------|
| Backend  | Kotlin     | https://github.com/archguard/archguard          |
| Frontend | TypeScript | https://github.com/archguard/archguard-frontend |
| Scanner  | Kotlin     | https://github.com/archguard/scanner            |


Module ?

```kotlin
repo("Backend") {
    module("diff_changes")
}
```

## Linter


### ArchLinter

simple assertion

```kotlin
module.name.should "casing"
```

### API 规则

- [x] 不应该存在被忽略（Ignore、Disabled）的测试用例 (#no-ignore-test)
- [ ] 允许存在重复的 assertion (#redundant-assertion) 

## Basic Blocks: Components and Artifacts

