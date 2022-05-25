package org.archguard.architecture.core

/**
 * **Component** is a abstract for all types components.
 * everything is component
 */
abstract class Component(
    val name: String,
    val type: ArchComponentType,
    var ports: List<Port> = listOf(),
    var childrens: List<Component> = listOf()
)

enum class ArchComponentType {
    SERVICE,
    PLUGIN,
    MODULE,
    PACKAGE,
    CLASSES,
}

abstract class Port(
    val protocols: List<Protocol> = listOf(),
    // link HTTP API, RPC API, CLI API
    val inbounds: List<Inbound>,
    // like database, HTTP Call
    val outbounds: List<Outbound>,
)

abstract class Protocol(val name: String)
abstract class Inbound(val name: String)
abstract class Outbound(val name: String)

class StringOutbound(name: String) : Outbound(name)
class StringInbound(name: String) : Inbound(name)
class StringProtocol(name: String = "Gradle") : Protocol(name)

class GradleInbound(name: String, artifact: String, group: String, version: String) : Inbound(name) {

}

class GradlePort(inbounds: List<GradleInbound> = listOf()) : Port(
    protocols = listOf<StringProtocol>(),
    inbounds = listOf<StringInbound>(),
    outbounds = listOf<StringOutbound>()
)

/**
 * **ExternalModuleDependencyComponent** is an external module dependency like `spring` in
 */
class ExternalModuleDependencyComponent(name: String) :
    Component(name, ArchComponentType.MODULE, listOf<GradlePort>()) {


}