package org.archguard.aaac.repl

import org.jetbrains.kotlinx.jupyter.ReplForJupyter
import org.jetbrains.kotlinx.jupyter.ReplForJupyterImpl
import org.jetbrains.kotlinx.jupyter.libraries.EmptyResolutionInfoProvider
import java.io.File

class ArchdocCompiler : BaseCompiler() {
    override fun makeEmbeddedRepl(): ReplForJupyter {
        val resolutionInfoProvider = EmptyResolutionInfoProvider

        val embeddedClasspath: MutableList<File> =
            System.getProperty("java.class.path").split(File.pathSeparator).map(::File).toMutableList();

        // load same ArchGuard DSL version
        embeddedClasspath += this.addArchGuardDsl()

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
        return ReplForJupyterImpl(
            resolutionInfoProvider,
            embeddedClasspath,
            isEmbedded = true,
            libraryResolver = dslLibS
        )
    }
}
