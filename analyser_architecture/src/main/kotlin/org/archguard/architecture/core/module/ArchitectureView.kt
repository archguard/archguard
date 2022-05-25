package org.archguard.architecture.core.module

import org.archguard.architecture.core.module.view.code.CodeArchitecture
import org.archguard.architecture.core.module.view.concept.ConceptArchitecture
import org.archguard.architecture.core.module.view.execution.ExecutionArchitecture
import org.archguard.architecture.core.module.view.module.ModuleArchitecture

class ArchitectureView(
    val conceptArchitecture: ConceptArchitecture,
    val moduleArchitecture: ModuleArchitecture,
    val executionArchitecture: ExecutionArchitecture,
    val codeArchitecture: CodeArchitecture
)

