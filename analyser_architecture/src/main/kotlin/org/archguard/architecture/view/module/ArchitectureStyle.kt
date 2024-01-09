package org.archguard.architecture.view.module

import kotlinx.serialization.Serializable
import org.archguard.architecture.detect.PotentialExecArch

@Serializable
sealed interface ArchitectureStyle {
    companion object {
        fun from(identPotential: PotentialExecArch): ArchitectureStyle {
            return LayeredArchitecture(listOf())
        }
    }
}

