package org.archguard.scanner.analyser

import org.archguard.scanner.analyser.sca.gradle.GradleFinder
import org.archguard.scanner.analyser.sca.maven.MavenFinder
import org.archguard.scanner.analyser.sca.npm.NpmFinder
import org.archguard.scanner.core.sca.CompositionDependency
import org.archguard.scanner.core.sca.PackageDependencies
import org.archguard.scanner.core.sca.ScaAnalyser
import org.archguard.scanner.core.sca.ScaContext
import java.util.UUID

class ScaAnalyser(override val context: ScaContext) : ScaAnalyser {
    private val client = context.client
    private val path = context.path

    override fun analyse(): List<CompositionDependency> {
        return when (context.language) {
            "java", "kotlin" -> {
                val depDeclarations = GradleFinder().process(path).toMutableList()
                depDeclarations += MavenFinder().process(path)
                depDeclarations.flatMap {
                    it.toCompositionDependency()
                }.toList()
            }
            "javascript", "typescript" -> {
                val depDeclarations = NpmFinder().process(path)
                depDeclarations.flatMap {
                    it.toCompositionDependency()
                }.toList()
            }
            else -> throw IllegalStateException("Unsupported language: ${context.language}")
        }.also {
            client.saveDependencies(it)
        }
    }

    private fun PackageDependencies.toCompositionDependency(): List<CompositionDependency> = dependencies.map {
        CompositionDependency(
            id = UUID.randomUUID().toString(),
            name = this.name,
            version = this.version,
            parentId = "",
            path = this.path,
            packageManager = this.packageManager,
            depName = it.name,
            depArtifact = it.artifact,
            depGroup = it.group,
            depScope = it.scope.toString(),
            depVersion = it.version
        )
    }
}

