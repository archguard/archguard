package com.thoughtworks.archguard.scanner.sourcecode

import chapi.domain.core.CodeDataStruct
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ClassRepositoryTest {
    @Test
    internal fun should_handle_typescript_naming_issue() {
        CodeDataStruct(
            Package = "InvokeGraph.index.default",

        )

        val clzRepo = ClassRepository("1", "typescript", "archguard")
//        clzRepo.saveClassElement()
    }
}