package org.archguard.architecture.core

class Architecture(
    val archStyle: ArchitectureStyle,
    val subSystem: List<SubSystem>,
    val codeStructureStyle: List<CodeStructureStyle>,
    val metadata: ArchMedataData
)

class ArchMedataData(
    // tech stacks, like Spring, Redis, Kafka, MyBatis, Dubbo
    val techStacks: List<String>,
    // language summary ?
    val lineCounts: List<String>,
)

// sub-systems with relations
class SubSystem(
    // inbound system ids
    val inbounds: List<String>,
    // examples: DDD
    val codeStructureStyle: List<CodeStructureStyle>,
    // examples: org.archguard.domain, org.archguard.infrastructure, org.archguard.interface, org.archguard.application
    val packageStructure: List<String>
)

// from GitTags
enum class DevelMethodology {
    // 频繁的 tag 发布
    Agile,
    // 没有 Tag
    Waterfall,
    Unknown
}

// from Git History
enum class ScmStrategy {
    //
    GitFlow,
}

// base on Framework mapping
enum class ArchitectureStyle {
    // 分层
    Layered,
    // 管道
    Pipeline,
    // 微内核
    MicroKernel,
    // 基于服务
    ServiceBased,
    // 事件驱动
    EventDriven,
    // 基于空间
    SpaceBased,
    // 编制驱动的面向服务
    OrchestrationDrivenServiceOriented,
    // 微服务
    Microservice,
    Unknown
}

// from file name: like repository, controller, services
enum class CodeStructureStyle {
    MVC,
    DDD,
    CLEAN
}
