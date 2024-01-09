package org.archguard.architecture.view.execution

class ExecutionArchitecture(
    val connectionType: ConnectionType = ConnectionType.INBOUND,
    val connectorStyle: ConnectorStyle = ConnectorStyle.HTTP,
    val components: List<Component> = listOf(),
)
