# Arch Scanner

[![CI](https://github.com/archguard/scanner/actions/workflows/ci.yaml/badge.svg)](https://github.com/archguard/scanner/actions/workflows/ci.yaml)
[![codecov](https://codecov.io/gh/archguard/scanner/branch/master/graph/badge.svg?token=RSAOWTRFMT)](https://codecov.io/gh/archguard/scanner)
![GitHub release (latest by date)](https://img.shields.io/github/v/release/archguard/scanner)


Scanner:

* scan_git - Git commit history scan
* scan_jacoco - Jacoco scan
* scan_bytecode - for JVM languages
* scan_test_badsmell
   * [x] Java  
      * [x] based on [https://github.com/phodal/chapi-tbs](https://github.com/phodal/chapi-tbs)
* scan_sourcecode
  * [x] Java
  * [-] Kotlin
     * [x] Annotation support
  * [-] C#
     * [x] Annotation support
  * [x] TypeScript/JavaScript (not full test)
     * [x] React support
     * [ ] Angular support
     * [ ] Vue support
     * [ ] HTTP API
        * [x] axios
        * [x] umi-request

## Features

### Scan git

1. scan Git history to count file changes
2. count `line_count` (lines of count) with `language` of Git Files
   1. if file size > 10 MB will be ignored
3. with `-loc` flag will count Method & Class LoC
4. with `cognitive_complexity` for Java or other languages

### Scan source code

1. analysis code
2. create code basic info:
    - "code_class",
    - "code_field",
    - "code_method",
    - "code_annotation",
    - "code_annotation_value",
3. create code relations:
    - "code_ref_class_fields",
    - "code_ref_class_methods",
    - "code_ref_class_parent",
    - "code_ref_class_fields",
    - "code_ref_method_callees",
    - "code_ref_class_dependencies",
4. create container (HTTP API) level info:
    - "container_demand"    for used HTTP API
    - "container_resource"  for provide HTTP API
    - "container_service"   container info                         

### Scan Test Badsmell (Java)

1. scan test code issue

### Scan jacoco (Java)

1. need to refactor

## Usage

### scan_git

introduction：`程序会扫描指定的 GIT 仓库， 并在命令当前目录下生成SQL文件 output.sql`

cmd：`./gradlew :scan_git:run --args="--path=.."`

### scan_jacoco

introduction：`扫描目标项目下的 jacoco.exec 文件， 将目标项目的覆盖率数据生成 SQL 文件 output.sql`

cmd：`./gradlew :scan_jacoco:run --args="--target-project=."`

### scan_sourcecode

introduction: scan source code with [Chapi](https://github.com/modernizing/chapi)

cmd: `java "-Ddburl=jdbc:mysql://localhost:3306/archguard?user=root&password=&useSSL=false" -jar scan_sourcecode-1.1.7-all.jar --system-id=8 --path=scan_git --language=kotlin`

API

RESTTemplate call

```
@Component
class QualityGateClientImpl(@Value("\${client.host}") val baseUrl: String) : QualityGateClient {
    override fun getQualityGate(qualityGateName: String): CouplingQualityGate {
        val couplingQualityGate = RestTemplate().getForObject("$baseUrl/api/quality-gate-profile/$qualityGateName", CouplingQualityGate::class.java)
        return couplingQualityGate ?: CouplingQualityGate(null, qualityGateName, emptyList(), null, null)
    }
}
```

`$baseUrl/api/quality-gate-profile/$qualityGateName` -> `@uri@/api/quality-gate-profile/@uri@`

License
---

scan_bytecode inspired by [https://github.com/fesh0r/fernflower](https://github.com/fesh0r/fernflower)

`languages.json` based on [https://github.com/boyter/scc](https://github.com/boyter/scc) with MIT LICENSE.

@2020~2022 Thoughtworks. This code is distributed under the MPL license. See `LICENSE` in this directory.
