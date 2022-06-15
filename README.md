# ArchGuard backend

[![CI](https://github.com/archguard/archguard/actions/workflows/ci.yaml/badge.svg)](https://github.com/archguard/archguard/actions/workflows/ci.yaml)
[![codecov](https://codecov.io/gh/archguard/archguard/branch/master/graph/badge.svg?token=QS5H866CWH)](https://codecov.io/gh/archguard/archguard)
[![GitHub release](https://img.shields.io/github/v/release/archguard/archguard?logo=git&logoColor=white)](https://github.com/archguard/archguard/releases)
[![languages](https://img.shields.io/badge/language-kotlin-blueviolet?logo=kotlin&logoColor=white)](https://www.kotlincn.net/)
[![Java support](https://img.shields.io/badge/Java-12+-green?logo=java&logoColor=white)](https://openjdk.java.net/)
[![License](https://img.shields.io/github/license/archguard/archguard?color=4D7A97&logo=opensourceinitiative&logoColor=white)](https://opensource.org/licenses/MIT)
[![Gitpod Ready-to-Code](https://img.shields.io/badge/Gitpod-ready--to--code-green?label=gitpod&logo=gitpod&logoColor=white)](https://gitpod.io/#https://github.com/archguard/archguard)

> ArchGuard is an architecture governance tool which can analysis architecture in container, component, code level, database, create architecture fitness functions, and test for architecture rules. 

Chinese: ArchGuard 是一个架构治理工具，用于管理和分析组织级别的软件架构。 结合 [C4 模型](https://c4model.com)，进行依赖分析，含容器级别（服务级别）、组件级别（/模块级别）、代码级别、数据库级别等。 
同时，可以创建系统的架构适应度函数，度量系统的各项指标。

- Document: [https://archguard.org/](https://archguard.org/)
- Roadmap: [Roadmap](https://github.com/archguard/archguard/discussions/5)
- Contribute: [Contribute to Archguard](https://archguard.org/contribution)
- SubProjects:
    - [ArchGuard Frontend](https://github.com/archguard/archguard-frontend)
    - [Chapi](https://github.com/modernizing/chapi) source code analysis


特性（Features）：

- **设计态**（DOING）
  - 架构设计、分析与治理 DSL
- **开发态**
  - 架构扫描
    - 扫描配置
    - 插件化规则定制
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
  - 持续集成
- **运行态**
  - APM（TODO）
- **架构工作台**（DOING）

Features：

- **Design State** (DOING)
  - Architecture Design, Analysis and Governance DSL
- **Development state**
  - Schema scan
    - Scan configuration
    - Plug-in rule customization
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
    - Volume dimension: oversized components
    - Coupling dimension: hub components, too deep calls, circular dependencies
    - Cohesive Dimension: Shotgun Modification
    - Redundant dimensions: redundant elements, overgeneralization
    - Quality dimension: test protection
    - Continuous Integration
- **Running state**
  - APM (TODO)
- **Architecture Workbench** (DOING)

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

| Features/Languages  | Java | Python | Go  | Kotlin | TypeScript | C   | C#  | Scala | C++ |
|---------------------|------|--------|-----|--------|------------|-----|-----|-------|-----|
| http api decl       | ✅    | 🆕     | 🆕  | ✅      | ✅          | 🆕  | ✅   | 🆕    | 🆕  |
| syntax parse        | ✅    | ✅      | ✅   | ✅      | ✅          | 🆕  | ✅   | ✅     | 🆕  |
| function call       | ✅    | 🆕     |     | ✅      | ✅          |     |     |       |     |
| arch/package        | ✅    |        |     | ✅      | ✅          |     | ✅   | ✅     |     |
| real world validate | ✅    |        |     |        | ✅          |     |     |       |     |

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
