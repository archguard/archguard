package org.archguard.architecture.view.module

import kotlinx.serialization.Serializable
import org.archguard.architecture.detect.PotentialExecArch
import org.archguard.architecture.view.module.mvc.Controller
import org.archguard.architecture.view.module.mvc.Model
import org.archguard.architecture.view.module.mvc.View
import org.archguard.architecture.view.module.shared.Dependency

@Serializable
data class MVCArchitecture(
    val models: List<Model>,
    val views: List<View>,
    val controllers: List<Controller>,
    val dependencies: List<Dependency>
) :
    ArchitectureStyle {
    override fun buildArchitecture(identPotential: PotentialExecArch) {

    }
}

