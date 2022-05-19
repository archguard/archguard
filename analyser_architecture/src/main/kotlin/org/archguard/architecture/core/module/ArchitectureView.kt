package org.archguard.architecture.core.module

import org.archguard.architecture.core.module.view.code.CodeArchitecture
import org.archguard.architecture.core.module.view.concept.ConceptArchitecture
import org.archguard.architecture.core.module.view.execution.ExecutionArchitecture
import org.archguard.architecture.core.module.view.module.LayeredArchitecture

class ArchitectureView(
    val conceptArchitecture: ConceptArchitecture,
    val layeredArchitecture: LayeredArchitecture,
    val executionArchitecture: ExecutionArchitecture,
    val codeArchitecture: CodeArchitecture
)

