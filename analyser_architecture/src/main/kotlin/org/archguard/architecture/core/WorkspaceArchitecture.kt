package org.archguard.architecture.core

/**
 * **WorkspaceArchitecture** is the analysis result of Workspace
 * @property archStyle the architecture style
 * @property subSystems the sub-systems, a sub system contains components
 * @property components the component sets
 * @property connections the relations of sub-systems or components
 * @property metadata the summary of other data
 * @property ability -ability from source code
 */
class WorkspaceArchitecture(
    val archStyle: ArchitectureStyle,
    // 对应现有的 System 相关模型，正好对应到子系统这个概念上。
    val subSystems: List<SubSystem>,
    // todo: rename component to Element?
    // 对于单体系统来说，它可以是一些模块
    var components: List<ArchComponent>,
    // 组件或系统之间的关系风格，connectors or connectorStyles
    val connections: List<Connection>,
    // 分析
    val metadata: ArchMedataData,
    // 1. generate from libraries
    // 2. generate from app types domain
    val ability: List<String>
)

/**
 * sub-systems with relations
 */
class SubSystem(
    // inbound system ids
    val inbounds: List<String>,
    // examples: DDD
    val codeStructureStyle: List<CodeStructureStyle>,
    // examples: org.archguard.domain, org.archguard.infrastructure, org.archguard.interface, org.archguard.application
    val packageStructure: List<String>,
    val component: List<ArchComponent>,
)

class Connection(
    val connectors: String,
    // `qualifiedName`
    val source: String,
    // `qualifiedName`
    val target: String,
    var connectionType: ConnectionType,
    val connectorStyles: ConnectorStyle,
)

// like ServiceLoader, ClassLoader
enum class ConnectionType {
    STATIC,
    DYNAMIC
}

enum class ConnectorStyle {
    HttpApi,
    RPC,
    SQLLink,
    Protocol,
    DependencyInjection,
}

class ArchMedataData(
    // tech stacks, like Spring, Redis, Kafka, MyBatis, Dubbo
    val techStacks: List<String>,
    // language summary ?
    val lineCounts: List<String>,
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

    // ?
    Serverless,

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

    // examples: https://github.com/domain-driven-design/ddd-lite-example
    ModuleDDD,
    DDD,
    CLEAN,
    UNKNOWN
}

enum class ComponentTypes {
    Module,
    Computation,
    SharedData,
    SeqFile,
    Filter,
    Process,
    SchedProcess,
    General
}

//// 进程间通信
//enum class IPCType {
//    Pipe,
//    Signal,
//    Semaphore,
//    MessageQueue,
//    SharedMemory,
//    IpcSocket
//}

enum class ConnectorType {
    Process,
    Pipe,
    DataAccess,
    FileIO,
    RemoteProcedureCall,
    ProcedureCall,
    RTScheduler,
    Http,
}
