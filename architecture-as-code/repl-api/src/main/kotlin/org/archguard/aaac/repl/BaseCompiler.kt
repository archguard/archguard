package org.archguard.aaac.repl

import org.jetbrains.kotlinx.jupyter.api.Code
import org.jetbrains.kotlinx.jupyter.messaging.DisplayHandler
import org.jetbrains.kotlinx.jupyter.repl.EvalResult

interface BaseCompiler {
    fun eval(
        code: Code,
        displayHandler: DisplayHandler? = null,
        jupyterId: Int = -1,
        storeHistory: Boolean = true,
    ): EvalResult
}