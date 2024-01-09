package org.archguard.architecture.view.module.ddd

import kotlinx.serialization.Serializable

@Serializable
class DomainLayer(
    val aggregates: List<Aggregate>,
    val repositories: List<Repository>,
    val domainServices: List<DomainService>,
    val factories: List<Factory>
)

@Serializable
class ApplicationLayer(val applicationServices: List<ApplicationService>)

@Serializable
class InfrastructureLayer(val adaptors: List<Adaptor>)

@Serializable
class InterfaceLayer(val controllers: List<Controller>)

@Serializable
data class ApplicationService(val name: String)
@Serializable
data class Aggregate(val name: String)

@Serializable
data class Repository(val name: String)

@Serializable
class DomainService(val name: String)

@Serializable
data class Factory(val name: String)

@Serializable
data class Controller (val name: String)

@Serializable
data class Adaptor (val name: String)