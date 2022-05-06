package org.archguard.analyser.sca

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import org.archguard.analyser.sca.gradle.GradleFinder
import org.archguard.analyser.sca.helper.Bean2Sql
import org.archguard.analyser.sca.helper.CompositionDependency
import org.archguard.analyser.sca.maven.MavenFinder
import org.archguard.analyser.sca.model.PackageDependencies
import org.archguard.analyser.sca.processor.JavaFinder
import org.archguard.analyser.sca.npm.NpmFinder
import java.io.File
import java.util.*

class Runner : CliktCommand() {
    private val path: String by option(help = "the path of target project").default(".")
    private val systemId: String by option(help = "system id").default("0")
    private val language: String by option(help = "language").default("")
    // suit for multiple languages
    private val multiLanguages: Boolean by option(help = "multiple languages").flag(default = false)

    override fun run() {
        val bean2Sql = Bean2Sql()
        when (language.lowercase()) {
            "java", "kotlin" -> {
                val depDeclarations = GradleFinder().process(path).toMutableList()
                depDeclarations += MavenFinder().process(path)

                val deps = depDeclarations.flatMap {
                    it.toCompositionDependency(systemId)
                }.toList()

                val string = bean2Sql.bean2Sql(deps)
                File("output.sql").writeText(string)
            }
            "javascript", "typescript" -> {
                val depDeclarations = NpmFinder().process(path)
                val deps = depDeclarations.flatMap {
                    it.toCompositionDependency(systemId)
                }.toList()

                val string = bean2Sql.bean2Sql(deps)
                File("output.sql").writeText(string)
            }
        }
    }
}

private fun PackageDependencies.toCompositionDependency(systemId: String): List<CompositionDependency> {
    return this.dependencies.map {
        CompositionDependency(
            id = UUID.randomUUID().toString(),
            systemId = systemId,
            name = this.name,
            version = this.version,
            parentId = "",
            packageManager = this.packageManager,
            depName = it.name,
            depArtifact = it.artifact,
            depGroup = it.group,
            depScope = it.scope.toString(),
            depVersion = it.version
        )
    }.toList()
}

fun main(args: Array<String>) = Runner().main(args)


