package org.archguard.aaac.repl.compiler

import org.jetbrains.kotlinx.jupyter.ReplForJupyter
import org.jetbrains.kotlinx.jupyter.ReplForJupyterImpl
import org.jetbrains.kotlinx.jupyter.libraries.EmptyResolutionInfoProvider
import java.io.File

class SlimRepl : BaseRepl() {
    override fun makeEmbeddedRepl(): ReplForJupyter {
        val resolutionInfoProvider = EmptyResolutionInfoProvider

        val embeddedClasspath: MutableList<File> =
            System.getProperty("java.class.path").split(File.pathSeparator).map(::File).toMutableList()

// todo: make classpath slim

//        val classpath = scriptCompilationClasspathFromContext(
//            "lib",
//            "api",
//            "shared-compiler",
//            "kotlin-stdlib",
//            "kotlin-reflect",
//            "kotlin-script-runtime",
//            classLoader = DependsOn::class.java.classLoader
//        )

        val dslLibS = resolveArchGuardLibs()

        return ReplForJupyterImpl(
            resolutionInfoProvider, embeddedClasspath, isEmbedded = true, libraryResolver = dslLibS,
            runtimeProperties = this.replRuntimeProperties
        )
    }
}
