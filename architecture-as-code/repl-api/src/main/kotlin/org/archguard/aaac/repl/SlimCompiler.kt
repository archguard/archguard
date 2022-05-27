package org.archguard.aaac.repl

import org.jetbrains.kotlinx.jupyter.ReplForJupyter
import org.jetbrains.kotlinx.jupyter.ReplForJupyterImpl
import org.jetbrains.kotlinx.jupyter.libraries.EmptyResolutionInfoProvider
import java.io.File

class SlimCompiler : BaseCompiler() {
    override fun makeEmbeddedRepl(): ReplForJupyter {
        val resolutionInfoProvider = EmptyResolutionInfoProvider

        val embeddedClasspath: MutableList<File> =
            System.getProperty("java.class.path").split(File.pathSeparator).map(::File).toMutableList()

        embeddedClasspath += this.addArchGuardDsl()

        return ReplForJupyterImpl(resolutionInfoProvider, embeddedClasspath, isEmbedded = true)
    }
}