package org.archguard.architecture.view.module.ddd

import org.archguard.architecture.view.module.Dependency
import org.archguard.architecture.view.module.ModuleArchitecture

class DDDArchitecture(
    val infra: org.archguard.architecture.view.module.ddd.InfrastructureLayer,
    val application: org.archguard.architecture.view.module.ddd.ApplicationLayer,
    val domainLayer: org.archguard.architecture.view.module.ddd.DomainLayer,
    val interfaceLayer: org.archguard.architecture.view.module.ddd.InterfaceLayer,
    val dependencies: List<org.archguard.architecture.view.module.Dependency>
) :
    org.archguard.architecture.view.module.ModuleArchitecture

class DomainLayer(
    val aggregates: List<org.archguard.architecture.view.module.ddd.Aggregate>,
    val repositories: List<org.archguard.architecture.view.module.ddd.Repository>,
    val domainServices: List<org.archguard.architecture.view.module.ddd.DomainService>,
    val factories: List<org.archguard.architecture.view.module.ddd.Factory>
)

class ApplicationLayer(val applicationServices: List<org.archguard.architecture.view.module.ddd.ApplicationService>)

class InfrastructureLayer(val adaptors: List<org.archguard.architecture.view.module.ddd.Adaptor>)

class InterfaceLayer(val controllers: List<org.archguard.architecture.view.module.ddd.Controller>)

class ApplicationService

class Aggregate

class Repository

class DomainService

class Factory

class Controller

class Adaptor
