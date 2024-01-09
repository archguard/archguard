package org.archguard.architecture.view.module

import kotlinx.serialization.Serializable
import org.archguard.architecture.detect.PotentialExecArch

@Serializable
sealed interface ModuleArchitecture {
    companion object {
        fun from(identPotential: PotentialExecArch): ModuleArchitecture {
            return LayeredArchitecture(listOf())
        }
    }
}

enum class DependencyType {
    Notify,
    Query,
    Call,
    Update
}