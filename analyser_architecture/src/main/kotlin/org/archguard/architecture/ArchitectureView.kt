package org.archguard.architecture

import kotlinx.serialization.Serializable
import org.archguard.architecture.graph.TreeNode
import org.archguard.architecture.view.code.CodeArchitecture
import org.archguard.architecture.view.code.RepositoryType
import org.archguard.architecture.view.code.Language
import org.archguard.architecture.view.concept.ConceptArchitecture
import org.archguard.architecture.view.execution.ExecutionArchitecture
import org.archguard.architecture.view.module.ArchitectureStyle
import org.archguard.architecture.view.module.LayeredArchitecture
import org.archguard.scanner.core.estimate.LanguageEstimate

@Serializable
class ArchitectureView(
    var conceptArchitecture: ConceptArchitecture = ConceptArchitecture(),
    var architectureStyle: ArchitectureStyle = LayeredArchitecture(listOf()),
    var executionArchitecture: ExecutionArchitecture = ExecutionArchitecture(),
    var codeArchitecture: CodeArchitecture = CodeArchitecture(
        Language.Java,
        RepositoryType.Interface
    ),
    var physicalStructure: PhysicalStructure = PhysicalStructure()
)

@Serializable
data class PhysicalStructure (
    var languageEstimate: List<LanguageEstimate> = listOf(),
    var codeStructure: TreeNode = TreeNode("root"),
)

