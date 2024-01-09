package org.archguard.architecture

import kotlinx.serialization.Serializable
import org.archguard.architecture.view.code.CodeArchitecture
import org.archguard.architecture.view.code.CodeType
import org.archguard.architecture.view.code.LangType
import org.archguard.architecture.view.concept.ConceptArchitecture
import org.archguard.architecture.view.execution.ExecutionArchitecture
import org.archguard.architecture.view.module.ModuleArchitecture
import org.archguard.architecture.view.module.LayeredArchitecture
import org.archguard.scanner.core.estimate.LanguageEstimate

@Serializable
class ArchitectureView(
    var conceptArchitecture: ConceptArchitecture = ConceptArchitecture(),
    var moduleArchitecture: ModuleArchitecture = LayeredArchitecture(listOf()),
    var executionArchitecture: ExecutionArchitecture = ExecutionArchitecture(),
    var codeArchitecture: CodeArchitecture = CodeArchitecture(
        LangType.Java,
        CodeType.Interface
    ),
    var languageSummary: List<LanguageEstimate> = listOf()
)

