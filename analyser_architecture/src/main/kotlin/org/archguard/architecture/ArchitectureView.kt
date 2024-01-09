package org.archguard.architecture

import org.archguard.architecture.view.code.CodeArchitecture
import org.archguard.architecture.view.code.CodeType
import org.archguard.architecture.view.code.LangType
import org.archguard.architecture.view.concept.ConceptArchitecture
import org.archguard.architecture.view.execution.ExecutionArchitecture
import org.archguard.architecture.view.module.ModuleArchitecture
import org.archguard.architecture.view.module.layer.LayeredArchitecture

class ArchitectureView(
    val conceptArchitecture: ConceptArchitecture = ConceptArchitecture(),
    val moduleArchitecture: ModuleArchitecture = LayeredArchitecture(listOf()),
    val executionArchitecture: ExecutionArchitecture = ExecutionArchitecture(),
    val codeArchitecture: CodeArchitecture = CodeArchitecture(
        LangType.Java,
        CodeType.Interface
    )
)

