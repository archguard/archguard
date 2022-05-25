package org.archguard.architecture.core.module.view.module.ddd

import org.archguard.architecture.core.module.view.module.Dependency
import org.archguard.architecture.core.module.view.module.ModuleArchitecture

class DDDArchitecture(
    val infra: InfrastructureLayer,
    val application: ApplicationLayer,
    val domainLayer: DomainLayer,
    val interfaceLayer: InterfaceLayer,
    val dependencies: List<Dependency>
) :
    ModuleArchitecture

class DomainLayer(
    val aggregates: List<Aggregate>,
    val repositories: List<Repository>,
    val domainServices: List<DomainService>,
    val factories: List<Factory>
)

class ApplicationLayer(val applicationServices: List<ApplicationService>)

class InfrastructureLayer(val adaptors: List<Adaptor>)

class InterfaceLayer(val controllers: List<Controller>)

class ApplicationService

class Aggregate

class Repository

class DomainService

class Factory

class Controller

class Adaptor
