package org.archguard.scanner.cost.count

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class FileWorkerKtTest {
    @Test
    @Disabled
    fun process_current_project() {
        process("src/main/kotlin/org/archguard/scanner/cost/count/LanguageService.kt")
    }
}