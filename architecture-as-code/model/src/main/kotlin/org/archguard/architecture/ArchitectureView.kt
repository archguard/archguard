package org.archguard.architecture

import kotlinx.serialization.Serializable
import org.archguard.architecture.graph.TreeNode
import org.archguard.architecture.view.code.CodeArchitecture
import org.archguard.architecture.view.code.RepositoryType
import org.archguard.architecture.view.execution.ExecutionArchitecture
import org.archguard.architecture.view.module.LayeredArchitecture
import org.archguard.context.LanguageEstimate
import org.archguard.context.ContainerService

@Serializable
class ArchitectureView(
    var conceptArchitecture: org.archguard.architecture.view.concept.ConceptArchitecture = org.archguard.architecture.view.concept.ConceptArchitecture(),
    var architectureStyle: org.archguard.architecture.view.module.ArchitectureStyle = LayeredArchitecture(),
    var executionArchitecture: ExecutionArchitecture = ExecutionArchitecture(),
    var codeArchitecture: CodeArchitecture = CodeArchitecture(
        RepositoryType.Service,
    ),
    var physicalStructure: PhysicalStructure = PhysicalStructure(),
    var outboundService: List<ContainerService> = listOf(),
)

@Serializable
data class PhysicalStructure (
    var languageEstimate: List<LanguageEstimate> = listOf(),
    var codeStructure: TreeNode = TreeNode("root"),
)

