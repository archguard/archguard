package org.archguard.archdoc.repl

import org.jetbrains.kotlinx.jupyter.EvalRequestData
import org.jetbrains.kotlinx.jupyter.ReplForJupyter
import org.jetbrains.kotlinx.jupyter.ReplForJupyterImpl
import org.jetbrains.kotlinx.jupyter.api.Code
import org.jetbrains.kotlinx.jupyter.libraries.EmptyResolutionInfoProvider
import org.jetbrains.kotlinx.jupyter.messaging.DisplayHandler
import java.io.File


class ArchdocCompiler {
    private fun makeEmbeddedRepl(): ReplForJupyter {
        val resolutionInfoProvider = EmptyResolutionInfoProvider

        val embeddedClasspath: List<File> =
            System.getProperty("java.class.path").split(File.pathSeparator).map(::File)
        return ReplForJupyterImpl(resolutionInfoProvider, embeddedClasspath, isEmbedded = true)
    }

    private val repl = makeEmbeddedRepl()

    fun eval(code: Code, displayHandler: DisplayHandler? = null, jupyterId: Int = -1, storeHistory: Boolean = true) =
        repl.eval(EvalRequestData(code, displayHandler, jupyterId, storeHistory))
}
