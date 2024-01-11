package org.archguard.architecture.view.module

import chapi.domain.core.CodeDataStruct
import kotlinx.serialization.Serializable
import org.archguard.architecture.detect.PotentialExecArch
import org.archguard.architecture.view.module.ddd.DomainLayer
import org.archguard.architecture.view.module.shared.Dependency

@Serializable
class DDDStyleArchitecture(
    val infra: List<CodeDataStruct>,
    val application: List<CodeDataStruct>,
    val domainLayer: DomainLayer,
    val interfaceLayer: List<CodeDataStruct>,
    val dependencies: List<Dependency>
) : ArchitectureStyle {
    override fun buildArchitecture(identPotential: PotentialExecArch) {

    }
}
