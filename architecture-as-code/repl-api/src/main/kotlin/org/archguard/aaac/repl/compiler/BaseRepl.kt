package org.archguard.aaac.repl.compiler

import org.archguard.dsl.base.model.ReactiveAction
import org.jetbrains.kotlinx.jupyter.EvalRequestData
import org.jetbrains.kotlinx.jupyter.ReplForJupyter
import org.jetbrains.kotlinx.jupyter.api.Code
import org.jetbrains.kotlinx.jupyter.libraries.LibraryResolver
import org.jetbrains.kotlinx.jupyter.messaging.DisplayHandler
import java.io.File

abstract class BaseRepl {
    // looking for dep path to add files
    abstract fun makeEmbeddedRepl(): ReplForJupyter

    private val repl: ReplForJupyter = this.makeEmbeddedRepl()

    fun setupArchGuardLibs() = File(ReactiveAction::class.java.protectionDomain.codeSource.location.toURI().path)

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

        val dslLibS = listOf(lib).toLibraries()
        return dslLibS
    }
}
