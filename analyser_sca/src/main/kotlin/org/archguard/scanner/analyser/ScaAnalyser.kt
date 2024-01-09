package org.archguard.scanner.analyser

import org.archguard.scanner.analyser.sca.cargo.CargoFinder
import org.archguard.scanner.analyser.sca.gomod.GoModFinder
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
        val packageDependencies = analysisByPackages()

        return packageDependencies.flatMap {
            it.toCompositionDependency()
        }.toList().also {
            client.saveDependencies(it)
        }
    }

    /**
     * This method, `analysisByPackages`, is used to analyze the dependencies of a project based on the language
     * specified in the context.
     * It returns a list of `PackageDependencies` objects, which represent the dependencies found in the project.
     *
     * @return A list of `PackageDependencies` objects representing the dependencies found in the project.
     * @throws IllegalStateException if the language specified in the context is not supported.
     */
    fun analysisByPackages(): List<PackageDependencies> {
        val packageDependencies = when (context.language) {
            "java", "kotlin" -> {
                GradleFinder().process(path) + MavenFinder().process(path)
            }

            "javascript", "typescript" -> {
                NpmFinder().process(path)
            }

            "golang" -> {
                GoModFinder().process(path)
            }

            "rust" -> {
                CargoFinder().process(path)
            }

            else -> throw IllegalStateException("Unsupported language: ${context.language}")
        }

        return packageDependencies
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

