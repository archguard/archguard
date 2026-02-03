# ArchGuard backend

[![CI](https://github.com/archguard/archguard/actions/workflows/ci.yaml/badge.svg)](https://github.com/archguard/archguard/actions/workflows/ci.yaml)
[![codecov](https://codecov.io/gh/archguard/archguard/branch/master/graph/badge.svg?token=QS5H866CWH)](https://codecov.io/gh/archguard/archguard)
[![GitHub release](https://img.shields.io/github/v/release/archguard/archguard?logo=git&logoColor=white)](https://github.com/archguard/archguard/releases)
[![languages](https://img.shields.io/badge/language-kotlin-blueviolet?logo=kotlin&logoColor=white)](https://www.kotlincn.net/)
[![Java support](https://img.shields.io/badge/Java-12+-green?logo=java&logoColor=white)](https://openjdk.java.net/)
![Maven Central](https://img.shields.io/maven-central/v/org.archguard.scanner/scanner_core)
![Docker Image Version](https://img.shields.io/docker/v/archguard/archguard-backend?sort=semver&style=flat&label=DockerHub%20Version&link=https%3A%2F%2Fhub.docker.com%2Fr%2Farchguard%2Farchguard-backend%2Ftags)

> ArchGuard is an architecture governance tool that can analysis architecture in container, component, code level, database, create architecture fitness functions, and test for architecture rules. 

Chinese: ArchGuard 是一个针对于微服务（分布式场景）下的架构工作台/治理工具。它可以帮助架构师、开发人员进行架构自助，自定义架构的洞察、
分析系统间的远程服务依赖情况、数据库依赖、API 依赖等。并根据一些架构治理模型，对现有系统提出改进建议。

- Document: [https://archguard.org/](https://archguard.org/)
- Roadmap: [Roadmap](https://github.com/archguard/archguard/discussions/5)
- Contribute: [Contribute to Archguard](https://archguard.org/contribution)
- SubProjects:
    - [ArchGuard Frontend](https://github.com/archguard/archguard-frontend)
    - [Chapi](https://github.com/modernizing/chapi) source code analysis
    - [ArchGuard Co-mate](https://github.com/unit-mesh/co-mate) an AI-powered architecture copilot, design and governance tools.
- Architecture:

![Architecture](https://archguard.org/architecture.svg)

You can use:

- [ArchGuard Library in Maven Central](https://search.maven.org/search?q=org.archguard) to integrate with your backend.
- [ArchGuard Scanner CLI](./scanner_cli) to scan your codebase, upload to your backend or ArchGuard backend.
- [ArchGuard Gradle Plugin](https://github.com/archguard/archguard-gradle-plugin) to scan your codebase in CI/CD.
- [ArchGuard Web](./server) is the backend of ArchGuard, it provides a RESTful API for frontend.

特性（Features）：

- **设计态**
  - 架构设计、分析与治理 DSL
  - Feakin: [https://github.com/feakin/fklang](https://github.com/feakin/fklang)
- **开发态**
  - 架构扫描
    - 扫描配置
    - 插件化规则定制
    - 规则化治理：Code Smell, Test Code Smell, SQL Smell, API Smell, Documentation Smell, etc.
  - 架构可视化
    - 基于 C4 模型的可视化分析
      - 上下文：API 服务地图（API 生产者支持语言：Java、Kotlin、C#，API 消费者支持语言：TypeScript/JavaScript、Kotlin、Java 等）
      - 容器分析。数据库地图（支持 MyBatis、JDBI、JPA）
      - 组件分析
      - 代码分析：支持级别模块、包、类、方法四个级别。
    - 高级分析 + 可视化
      - 系统不稳定性模块分析。
      - 容器间：精准测试/变化分析
  - 架构指标（单体DONE，分布式DOING）
    - 体量维度：过大的组件
    - 耦合维度：枢纽组件，过深调用，循环依赖
    - 内聚维度：霰弹式修改
    - 冗余维度：冗余元素，过度泛化
    - 质量维度：测试保护
  - 代码分析
    - CLOCO：代码复杂度 [#79](https://github.com/archguard/archguard/issues/79)
    - SCA 分析
    - OpenAPI 分析
    - Architecture analysis
- **运行态**
  - APM（TODO）
- **架构工作台**

Features：

- **Design State**
  - Architecture Design, Analysis and Governance DSL
  - Feakin: [https://github.com/feakin/fklang](https://github.com/feakin/fklang)
- **Development state**
  - Schema scan
    - Scan configuration
    - Plug-in rule customization
    - Rule-based governance: Code Smell, Test Code Smell, SQL Smell, API Smell, Documentation Smell, etc.
  - Architecture visualization
    - Visual analysis based on C4 model
      - Context: API service map (API producer supported languages: Java, Kotlin, C#, API consumer supported languages: TypeScript/JavaScript, Kotlin, Java, etc.)
      - Container analysis. Database map (support MyBatis, JDBI, JPA)
      - Component analysis
      - Code analysis: supports four levels of modules, packages, classes, and methods.
    - Advanced Analysis + Visualization
      - System instability module analysis.
      - Between containers: precise testing/variation analysis
  - Architecture metrics (single DONE, distributed DOING)
    - Volume dimension: oversize components
    - Coupling dimension: hub components, too deep calls, circular dependencies
    - Cohesive Dimension: Shotgun Modification
    - Redundant dimensions: redundant elements, overgeneralization
    - Quality dimension: test protection
    - Continuous Integration
  - External analysis
    - CLOCO: Code Complexity [#79](https://github.com/archguard/archguard/issues/79)
    - SCA analysis
    - OpenAPI analysis
    - Architecture analysis
- **Running state**
  - APM (TODO)
- **Architecture Workbench**

Screenshots:

<table>
  <tr>
    <td><img src="https://archguard.org/assets/screenshots/archguard-20-overview.png"  alt="1" width = 480px /></td>
    <td><img src="https://archguard.org/assets/screenshots/archguard-20-apilist.png" alt="2" width = 480px /></td>
   </tr> 
   <tr>
      <td><img src="https://archguard.org/assets/screenshots/archguard-20-class.png" alt="3" width = 480px /></td>
      <td><img src="https://archguard.org/assets/screenshots/archguard-20-servicesmap.png" alt="4" width = 480px  /></td>
  </tr>
</table>

Languages parse by [Chapi](https://github.com/modernizing/chapi)

| Features/Languages  | Java | Python | Go | Kotlin | TypeScript | C  | C# | Scala | C++ |
|---------------------|------|--------|----|--------|------------|----|----|-------|-----|
| http api decl       | ✅    | 🆕     | 🆕 | ✅      | ✅          | 🆕 | ✅  | 🆕    | 🆕  |
| syntax parse        | ✅    | ✅      | ✅  | ✅      | ✅          | ✅  | ✅  | ✅     | 🆕  |
| function call       | ✅    | 🆕     | ✅  | ✅      | ✅          |    |    |       |     |
| arch/package        | ✅    |        |    | ✅      | ✅          | 🆕 | ✅  | ✅     |     |
| real world validate | ✅    |        |    |        | ✅          |    |    |       |     |

## Custom Backend

case example: 

- [https://github.com/unit-mesh/co-unit](https://github.com/unit-mesh/co-unit) by Rust language

use [Scanner CLI](./scanner_cli) you can customize your backend. For more detail, see in: [ArchGuardHttpClient](scanner_cli/src/main/kotlin/org/archguard/scanner/ctl/client/ArchGuardHttpClient.kt)

HTTP examples:

```http request
POST http://127.0.0.1:8765/scanner/:systemId/reporting/class-items

POST http://127.0.0.1:8765/scanner/:systemId/reporting/openapi

POST http://127.0.0.1:8765/scanner/:systemId/reporting/container-services

POST http://127.0.0.1:8765/scanner/:systemId/reporting/datamap-relations

...
```

## MCP Server (Model Context Protocol)

🚀 **New Feature Proposal**: Expose ArchGuard's architecture linter as an MCP server for AI-assisted development!

The MCP server would enable AI assistants (Claude, ChatGPT, etc.) and development tools to leverage ArchGuard's linting capabilities through a lightweight, standardized interface.

**Key Benefits:**
- ⚡ **Lightweight**: <100MB memory, <2s startup (vs. full server)
- 🤖 **AI Integration**: Works with Claude, ChatGPT, GitHub Copilot
- 🔌 **Standard Protocol**: Compatible with any MCP-enabled tool
- 🎯 **Stateless**: Quick analysis without database overhead

**Documentation:**
- [Detailed Proposal](doc/mcp/mcp-server-proposal.md) - Full specification and architecture
- [GitHub Issue Template](doc/mcp/mcp-server-issue.md) - Ready to submit feature request
- [Implementation Guide](doc/mcp/mcp-server-implementation-guide.md) - Step-by-step development guide

This would complement the existing server and CLI, adding a modern AI-friendly interface for architectural governance.

### Chat

关注我们：

<img src="https://archguard.org/wechat.jpg" width="380" height="380" alt="wechat">

欢迎加入我们：

<img src="https://archguard.org/qrcode.jpg" width="380" height="480" alt="wechat">

（PS：如果群满，请添加微信 `phodal02`，并注明 ArchGuard）

## Thanks

JetBrains support:

![JetBrains Logo (Main) logo](https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.svg)

License
---

This code is distributed under the MIT license. See `LICENSE` in this directory.
