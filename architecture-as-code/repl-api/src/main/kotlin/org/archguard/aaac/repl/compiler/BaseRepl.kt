package org.archguard.aaac.repl.compiler

import org.jetbrains.kotlinx.jupyter.EvalRequestData
import org.jetbrains.kotlinx.jupyter.ReplForJupyter
import org.jetbrains.kotlinx.jupyter.RuntimeKernelProperties
import org.jetbrains.kotlinx.jupyter.api.Code
import org.jetbrains.kotlinx.jupyter.libraries.LibraryResolver
import org.jetbrains.kotlinx.jupyter.messaging.DisplayHandler

abstract class BaseRepl {
//    private val logger = LoggerFactory.getLogger(this.javaClass)

    // looking for dep path to add files
    abstract fun makeEmbeddedRepl(): ReplForJupyter

    private val repl: ReplForJupyter = this.makeEmbeddedRepl()

//    fun setupArchGuardLibs(): File {
//        val location = ReactiveAction::class.java.protectionDomain.codeSource.location
//        logger.info("ArchGuard lib paths: $location")
//        return File(location.toURI().path)
//    }

    val replRuntimeProperties = RuntimeKernelProperties(
        mapOf(
            "version" to "0.11.0.89.dev1",
            "currentBranch" to "stable-kotlin",
            "currentSha" to "3c9c34dae3d4a334809d3bb078012b743b2bd618",
            "librariesFormatVersion" to "2",
            "jvmTargetForSnippets" to "12"
        )
    )

    fun eval(code: Code, displayHandler: DisplayHandler? = null, jupyterId: Int = -1, storeHistory: Boolean = true) =
        repl.eval(EvalRequestData(code, displayHandler, jupyterId, storeHistory))

    protected fun resolveArchGuardLibs(): LibraryResolver {
        // setup archguard magic
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
