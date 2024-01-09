package org.archguard.architecture.core

/**
 * **WorkspaceArchitecture** is the analysis result of Workspace
 * @property archStyle the architecture style
 * @property subSystems the sub-systems, a sub system contains components
 * @property components the component sets
 * @property connections the relations of sub-systems or components
 * @property overview the summary of other data
 * @property ability -ability from source code
 */
class WorkspaceArchitecture(
    private val archStyle: ArchitectureStyle,
    // 对于单体系统来说，它可以是一些模块。系统可能是一个组件。
    private var components: List<Component>,
    // 组件或系统之间的关系风格，connectors or connectorStyles
    private val connections: List<Connection>,
    // 数据
    private val overview: ArchOverview,
    // 1. generate from libraries
    // 2. generate from app types domain
    // private val ability: List<String>
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
    val component: List<Component>,
)

class Connection(
    val connectors: String,
    // `qualifiedName`, component id
    val source: String,
    // `qualifiedName`, component id
    val target: String,
    var connectionType: ConnectionType,
    val connectorStyles: ConnectorStyle,
)

// like ServiceLoader, ClassLoader
enum class ConnectionType {
    STATIC,
    DYNAMIC
}

class ArchOverview(
    // tech stacks, like Spring, Redis, Kafka, MyBatis, Dubbo
    val techStacks: List<String>,
    // language summary ?
    val lineCounts: List<String>,
)

/// from GitTags
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

///
enum class CodeStructureStyle {
    MVC,
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

/**
 * Represents the types of inter-process communication (IPC) mechanisms.
 *
 * The IPCType enum class provides a set of options for different IPC mechanisms that can be used for communication
 * between processes. These mechanisms enable processes running on the same or different systems to exchange data
 * and synchronize their actions.
 *
 * The available IPC types are:
 *  - Pipe: A unidirectional communication channel that allows the transfer of data between two related processes.
 *  - Signal: A software interrupt that can be sent to a process to notify it of an event or to request its attention.
 *  - Semaphore: A synchronization primitive that controls access to a shared resource by multiple processes.
 *  - MessageQueue: A message passing mechanism that allows processes to send and receive messages.
 *  - SharedMemory: A region of memory that can be accessed by multiple processes, enabling them to share data.
 *  - IpcSocket: A bidirectional communication channel that allows processes to communicate over a network.
 *
 * Each IPC type has its own characteristics, advantages, and limitations. The choice of IPC mechanism depends on the
 * specific requirements of the application and the environment in which it is running.
 *
 * Usage:
 *  val ipcType: IPCType = IPCType.Pipe
 *  println("Selected IPC type: $ipcType")
 *
 * Note: This enum class is part of the Kotlin language and can be used directly without any additional imports.
 */
enum class IPCType {
    Pipe,
    Signal,
    Semaphore,
    MessageQueue,
    SharedMemory,
    IpcSocket
}

// 多个组件间的交互
enum class ConnectorStyle {
    HttpApi,
    RPC,
    SQLLink,
    Protocol,
    DependencyInjection,
}

// 包含多个组件的交互，组件的输入输出
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
