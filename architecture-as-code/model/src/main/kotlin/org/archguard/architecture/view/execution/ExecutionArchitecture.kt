package org.archguard.architecture.view.execution

import kotlinx.serialization.Serializable

@Serializable
class ExecutionArchitecture(
    val connectionType: ConnectionType = ConnectionType.INBOUND,
    val connectorStyle: ConnectorStyle = ConnectorStyle.HTTP,
    val components: List<org.archguard.architecture.view.execution.Component> = listOf(),
)
