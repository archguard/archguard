package org.archguard.architecture.view.module.ddd

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