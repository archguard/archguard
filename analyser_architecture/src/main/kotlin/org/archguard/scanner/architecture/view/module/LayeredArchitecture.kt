package org.archguard.scanner.architecture.view.module

import kotlinx.serialization.Serializable
import org.archguard.scanner.architecture.detect.PotentialExecArch
import org.archguard.scanner.architecture.view.module.layer.MLayer

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
