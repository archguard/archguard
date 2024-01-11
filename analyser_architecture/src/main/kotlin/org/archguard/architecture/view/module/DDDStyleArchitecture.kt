package org.archguard.architecture.view.module

import chapi.domain.core.CodeDataStruct
import kotlinx.serialization.Serializable
import org.archguard.architecture.detect.PotentialExecArch
import org.archguard.architecture.view.module.ddd.DomainLayer
import org.archguard.architecture.view.module.shared.Dependency

@Serializable
class DDDStyleArchitecture() : ArchitectureStyle {
    val infra: List<CodeDataStruct> = listOf()
    val application: List<CodeDataStruct> = listOf()
    val domainLayer: DomainLayer = DomainLayer()
    val interfaceLayer: List<CodeDataStruct> = listOf()
    val dependencies: List<Dependency> = listOf()
    override fun canBeApplied(identPotential: PotentialExecArch): Boolean {
        return false
    }
}
