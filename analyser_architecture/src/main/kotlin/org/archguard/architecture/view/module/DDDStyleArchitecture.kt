package org.archguard.architecture.view.module

import kotlinx.serialization.Serializable
import org.archguard.architecture.view.module.ddd.ApplicationLayer
import org.archguard.architecture.view.module.ddd.DomainLayer
import org.archguard.architecture.view.module.ddd.InfrastructureLayer
import org.archguard.architecture.view.module.ddd.InterfaceLayer
import org.archguard.architecture.view.module.shared.Dependency

@Serializable
class DDDStyleArchitecture(
    val infra: InfrastructureLayer,
    val application: ApplicationLayer,
    val domainLayer: DomainLayer,
    val interfaceLayer: InterfaceLayer,
    val dependencies: List<Dependency>
) : ArchitectureStyle
