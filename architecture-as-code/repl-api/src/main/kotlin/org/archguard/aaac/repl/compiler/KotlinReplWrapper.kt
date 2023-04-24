package org.archguard.aaac.repl.compiler

import org.jetbrains.kotlinx.jupyter.*
import org.jetbrains.kotlinx.jupyter.api.Code
import org.jetbrains.kotlinx.jupyter.libraries.EmptyResolutionInfoProvider
import org.jetbrains.kotlinx.jupyter.libraries.LibraryResolver
import org.jetbrains.kotlinx.jupyter.messaging.NoOpDisplayHandler
import org.jetbrains.kotlinx.jupyter.repl.creating.createRepl
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.script.experimental.jvm.util.KotlinJars

class KotlinReplWrapper {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    private val repl: ReplForJupyter

    init {
        this.repl = this.makeEmbeddedRepl()
    }

    private fun makeEmbeddedRepl(): ReplForJupyter {
        val property = System.getProperty("java.class.path")
        var embeddedClasspath: MutableList<File> = property.split(File.pathSeparator).map(::File).toMutableList()

        val isInRuntime = embeddedClasspath.size == 1
        if (isInRuntime) {
            System.setProperty("kotlin.script.classpath", property)

            val compiler = KotlinJars.compilerClasspath
            if (compiler.isNotEmpty()) {
                val tempdir = compiler[0].parent
                embeddedClasspath =
                    File(tempdir).walk(FileWalkDirection.BOTTOM_UP).sortedBy { it.isDirectory }.toMutableList()
            }
        }

        embeddedClasspath = embeddedClasspath.distinctBy { it.name } as MutableList<File>
        logger.info("classpath: $embeddedClasspath")

        return createRepl(
            EmptyResolutionInfoProvider,
            embeddedClasspath,
            libraryResolver = resolveArchGuardDsl(),
            displayHandler = NoOpDisplayHandler,
            isEmbedded = true,
            runtimeProperties = RuntimeKernelProperties(
                mapOf(
                    "version" to " 0.11.0.348",
                    "currentBranch" to "stable-kotlin",
                    "currentSha" to "d349508fbf0e94bf2f76435fcc8534cfa2bea380",
                    "librariesFormatVersion" to "2",
                    "jvmTargetForSnippets" to "11"
                )
            )
        )
    }

    fun eval(code: Code, jupyterId: Int = -1, storeHistory: Boolean = true) =
        repl.evalEx(EvalRequestData(code, jupyterId, storeHistory))

    companion object {
        fun resolveArchGuardDsl(): LibraryResolver {
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
}

