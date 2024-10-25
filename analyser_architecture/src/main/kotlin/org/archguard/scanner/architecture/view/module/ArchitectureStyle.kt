package org.archguard.scanner.architecture.view.module

import kotlinx.serialization.Serializable
import org.archguard.scanner.architecture.detect.PotentialExecArch

@Serializable
sealed interface ArchitectureStyle {
    fun canBeApplied(identPotential: PotentialExecArch): Boolean

    companion object {
        fun from(identPotential: PotentialExecArch): ArchitectureStyle {
            // get all the potential architecture styles
            val styles = listOf(
                PipesAndFilterArchitecture(),
                DDDStyleArchitecture(),
                MVCArchitecture(),
                LayeredArchitecture()
            )

            // find the first style that can be applied
            return styles.firstOrNull { it.canBeApplied(identPotential) }
                ?: LayeredArchitecture()
        }
    }
}

