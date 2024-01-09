package org.archguard.architecture

import org.archguard.architecture.view.code.CodeArchitecture
import org.archguard.architecture.view.concept.ConceptArchitecture
import org.archguard.architecture.view.execution.ExecutionArchitecture
import org.archguard.architecture.view.module.ModuleArchitecture

class ArchitectureView(
    val conceptArchitecture: ConceptArchitecture,
    val moduleArchitecture: ModuleArchitecture,
    val executionArchitecture: ExecutionArchitecture,
    val codeArchitecture: CodeArchitecture
)

