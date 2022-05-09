# ArchGuard ArchDoc

ArchDoc is interactive architecture as code tools.

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

## API

双向 API 验证

### xxAPI

$archdoc$webapi:start or 参数名 as start ?

API: /blog/sample

| 参数名       | 必选  | 类型     | 说明                                                                 |
|-----------|-----|--------|--------------------------------------------------------------------|
| app_id    | 是   | string | 系统分配的授权应用APP_ID.                                                   |
| timestamp | 是   | string | 时间戳。毫秒级的时间戳，时效性：7天                                                 |
| nonce     | 是   | string | 随机数，长度为5                                                           |
| signature | 是   | string | 加密签名。md5(md5(appId + nonce + timestamp) + appToken)，其中md5生成32长度，小写 |

## 分层架构

类似于 ArchUnit ？ 如 MVC，DDD

```kotlin
layered {
    prefixId("org.archguard")
    component("controller").dependentOn component("service")
    component("service").dependentOn component("repository")
}
```

## Concept

DDD like? of MEAF ?

```
concept {
    
}
```

## Basic Blocks: Components and Artifacts

? Architecture DSL ?

## Code Engine

like: [https://github.com/Kotlin/kotlin-jupyter](https://github.com/Kotlin/kotlin-jupyter)

## Include Syntax

- `@file:DependsOn(<coordinates>)`
- `@file:Repository(<absolute-path>)`

## Arch Syntax 

Context API ?

Query API:

```
var a = class("Lookbook").name
```

```
linter("webapi").lint(a)
```

