package org.archguard.codedb.fitness

import org.jetbrains.kotlinx.jupyter.*
import org.jetbrains.kotlinx.jupyter.api.Code
import org.jetbrains.kotlinx.jupyter.libraries.*
import org.jetbrains.kotlinx.jupyter.messaging.CommManagerImpl
import org.jetbrains.kotlinx.jupyter.messaging.DisplayHandler
import org.jetbrains.kotlinx.jupyter.messaging.NoOpDisplayHandler
import org.jetbrains.kotlinx.jupyter.repl.creating.MockJupyterConnection
import org.jetbrains.kotlinx.jupyter.repl.creating.createRepl
import org.jetbrains.kotlinx.jupyter.startup.KernelConfig
import org.jetbrains.kotlinx.jupyter.startup.createKernelPorts
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.script.experimental.jvm.util.KotlinJars

const val standardResolverBranch = "master"

class KotlinReplWrapper {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    private val repl: ReplForJupyter
    val standardResolverRuntimeProperties = object : ReplRuntimeProperties by defaultRuntimeProperties {
        override val currentBranch: String
            get() = standardResolverBranch
    }


    init {
        this.repl = this.makeEmbeddedRepl()
    }

    fun makeEmbeddedRepl(): ReplForJupyter {
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

        val kernelConfig = KernelConfig(
            ports = createKernelPorts { socket -> 8080 },
            transport = "tcp",
            signatureScheme = "hmac1-sha256",
            signatureKey = "",
            scriptClasspath = embeddedClasspath,
            homeDir = null,
        )

        val connection = MockJupyterConnection
        val commManger = CommManagerImpl(connection)
//        val notebook = NotebookImpl(replRuntimeProperties, connection, commManger)
//        val librariesScanner = LibrariesScanner(notebook)


        val standardResolutionInfoProvider = getDefaultClasspathResolutionInfoProvider()
        val resolver = getStandardResolver(".", standardResolutionInfoProvider)

//        val standardResolutionInfoProvider = getDefaultClasspathResolutionInfoProvider()
        return createRepl(
            standardResolutionInfoProvider,
            embeddedClasspath,
            isEmbedded = true,
            homeDir = null,
            runtimeProperties = standardResolverRuntimeProperties,
            displayHandler = NoOpDisplayHandler,
            mavenRepositories = defaultRepositories,
            libraryResolver = resolver
        )
    }

    fun eval(code: Code, jupyterId: Int = -1, storeHistory: Boolean = true) =
        repl.eval(EvalRequestData(code, jupyterId, storeHistory))

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
