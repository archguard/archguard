package org.archguard.aaac.repl.compiler

import org.jetbrains.kotlinx.jupyter.EvalRequestData
import org.jetbrains.kotlinx.jupyter.KernelConfig
import org.jetbrains.kotlinx.jupyter.ReplForJupyter
import org.jetbrains.kotlinx.jupyter.ReplForJupyterImpl
import org.jetbrains.kotlinx.jupyter.RuntimeKernelProperties
import org.jetbrains.kotlinx.jupyter.api.Code
import org.jetbrains.kotlinx.jupyter.libraries.EmptyResolutionInfoProvider
import org.jetbrains.kotlinx.jupyter.libraries.LibraryResolver
import org.jetbrains.kotlinx.jupyter.messaging.DisplayHandler
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.script.experimental.jvm.util.classpathFromClassloader

class FullRepl : BaseRepl() {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    private val repl: ReplForJupyter
    private val replRuntimeProperties = RuntimeKernelProperties(
        mapOf(
            "version" to "0.11.0.89.dev1",
            "currentBranch" to "stable-kotlin",
            "currentSha" to "3c9c34dae3d4a334809d3bb078012b743b2bd618",
            "librariesFormatVersion" to "2",
            "jvmTargetForSnippets" to "11"
        )
    )

    init {
        this.repl = this.makeEmbeddedRepl()
    }

    fun makeEmbeddedRepl(): ReplForJupyter {
        val embeddedClasspath: MutableList<File> =
            System.getProperty("java.class.path").split(File.pathSeparator).map(::File).toMutableList()

        val cl = ClassLoader.getSystemClassLoader()
        val cp = classpathFromClassloader(cl)

//        val classpath = scriptCompilationClasspathFromContext(
//            "lib",
//            "api",
//            "shared-compiler",
//            "kotlin-stdlib",
//            "kotlin-reflect",
//            "kotlin-script-runtime",
//            classLoader = DependsOn::class.java.classLoader
//        )

        logger.info("embeddedClasspath: $embeddedClasspath")
        logger.info("classLoader: $cp")
//        logger.info("classpath: $classpath")

        val dslLibS = resolveArchGuardLibs()
        val config = KernelConfig(
            ports = listOf(8080),
            transport = "tcp",
            signatureScheme = "hmac1-sha256",
            signatureKey = "",
            scriptClasspath = embeddedClasspath,
            homeDir = File(""),
            libraryResolver = dslLibS,
            resolutionInfoProvider = EmptyResolutionInfoProvider,
        )

        return ReplForJupyterImpl(config, this.replRuntimeProperties)
    }

    fun eval(code: Code, displayHandler: DisplayHandler? = null, jupyterId: Int = -1, storeHistory: Boolean = true) =
        repl.eval(EvalRequestData(code, displayHandler, jupyterId, storeHistory))

    private fun resolveArchGuardLibs(): LibraryResolver {
        val lib = "archguard" to """
        {
            "imports": [
                "org.archguard.dsl.*"
            ],
            "init": []
        }
            """.trimIndent()

        return listOf(lib).toLibraries()
    }
}
