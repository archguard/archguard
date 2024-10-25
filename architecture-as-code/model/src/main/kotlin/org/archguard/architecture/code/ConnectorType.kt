package org.archguard.architecture.code

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