package org.archguard.scanner.architecture

import kotlinx.serialization.Serializable
import org.archguard.scanner.architecture.graph.TreeNode
import org.archguard.scanner.architecture.view.code.CodeArchitecture
import org.archguard.scanner.architecture.view.code.RepositoryType
import org.archguard.scanner.architecture.view.concept.ConceptArchitecture
import org.archguard.scanner.architecture.view.execution.ExecutionArchitecture
import org.archguard.scanner.architecture.view.module.ArchitectureStyle
import org.archguard.scanner.architecture.view.module.LayeredArchitecture
import org.archguard.scanner.core.estimate.LanguageEstimate
import org.archguard.scanner.core.sourcecode.ContainerService

@Serializable
class ArchitectureView(
    var conceptArchitecture: ConceptArchitecture = ConceptArchitecture(),
    var architectureStyle: ArchitectureStyle = LayeredArchitecture(),
    var executionArchitecture: ExecutionArchitecture = ExecutionArchitecture(),
    var codeArchitecture: CodeArchitecture = CodeArchitecture(
        RepositoryType.Service
    ),
    var physicalStructure: PhysicalStructure = PhysicalStructure(),
    var outboundService: List<ContainerService> = listOf(),
)

@Serializable
data class PhysicalStructure (
    var languageEstimate: List<LanguageEstimate> = listOf(),
    var codeStructure: TreeNode = TreeNode("root"),
)

