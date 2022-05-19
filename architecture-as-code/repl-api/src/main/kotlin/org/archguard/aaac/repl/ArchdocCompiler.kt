package org.archguard.aaac.repl

import org.archguard.dsl.model.ReactiveAction
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

        val embeddedClasspath: MutableList<File> =
            System.getProperty("java.class.path").split(File.pathSeparator).map(::File).toMutableList();

        // load same ArchGuard DSL version
        embeddedClasspath += addArchGuardDsl()

        // setup archguard magic
        val lib = "archguard" to """
    {
        "imports": [
            "org.archguard.dsl.*"
        ],
        "init": []
    }
        """.trimIndent()

        val dslLibS = listOf(lib).toLibraries()
        return ReplForJupyterImpl(resolutionInfoProvider, embeddedClasspath, isEmbedded = true, libraryResolver = dslLibS)
    }

    // looking for dep path to add files
    private fun addArchGuardDsl() = File(ReactiveAction::class.java.protectionDomain.codeSource.location.toURI().path)

    private val repl = makeEmbeddedRepl()

    fun eval(code: Code, displayHandler: DisplayHandler? = null, jupyterId: Int = -1, storeHistory: Boolean = true) =
        repl.eval(EvalRequestData(code, displayHandler, jupyterId, storeHistory))
}
