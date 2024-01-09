package org.archguard.architecture.detect

import chapi.domain.core.CodeDataStruct
import kotlinx.serialization.Serializable
import org.archguard.architecture.core.CodeStructureStyle
import org.archguard.architecture.core.ConnectorType

/**
 * 潜在的架构元素，后续需要根据这个继续分析
 */
@Serializable
data class PotentialExecArch(
    var layeredStyle: CodeStructureStyle = CodeStructureStyle.UNKNOWN,
    var protocols: List<OutboundProtocol> = listOf(),
    var appTypes: List<AppType> = listOf(),
    var connectorTypes: List<ConnectorType> = listOf(),
    var coreStacks: List<String> = listOf(),
    var concepts: List<CodeDataStruct> = listOf()
)