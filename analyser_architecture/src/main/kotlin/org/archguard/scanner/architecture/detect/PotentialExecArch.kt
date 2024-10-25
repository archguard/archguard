package org.archguard.scanner.architecture.detect

import chapi.domain.core.CodeDataStruct
import kotlinx.serialization.Serializable
import org.archguard.scanner.architecture.core.CodeStructureStyle
import org.archguard.scanner.architecture.core.ConnectorType
import org.archguard.scanner.architecture.graph.TreeNode

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
    var concepts: List<CodeDataStruct> = listOf(),
    var physicalStructure: TreeNode = TreeNode("root"),
    var dataStructures: List<CodeDataStruct> = listOf(),
)
