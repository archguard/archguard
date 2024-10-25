package org.archguard.architecture.view.module

import kotlinx.serialization.Serializable
import org.archguard.architecture.detect.PotentialExecArch
import org.archguard.architecture.view.module.layer.MLayer

/**
 * we assume a module in maven or gradle should be a subsystem.
 * So in module architecture have one or more layers.
 */
@Serializable
class LayeredArchitecture : ArchitectureStyle {
    val layers: List<MLayer> = listOf()

    override fun canBeApplied(identPotential: PotentialExecArch): Boolean {
        return false
    }
}
