package org.archguard.architecture.view.module

import kotlinx.serialization.Serializable
import org.archguard.architecture.detect.PotentialExecArch
import org.archguard.architecture.view.module.mvc.Controller
import org.archguard.architecture.view.module.mvc.Model
import org.archguard.architecture.view.module.mvc.View
import org.archguard.architecture.view.module.shared.Dependency

@Serializable
class MVCArchitecture : ArchitectureStyle {
    val models: List<Model> = listOf()
    val views: List<View> = listOf()
    val controllers: List<Controller> = listOf()
    val dependencies: List<Dependency> = listOf()

    override fun canBeApplied(identPotential: PotentialExecArch): Boolean {
        return false
    }
}

