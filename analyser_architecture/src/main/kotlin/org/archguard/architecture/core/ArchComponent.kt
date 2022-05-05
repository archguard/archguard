package org.archguard.architecture.core

// todo: add Component Layout
// like http
class ArchComponent(
    val name: String,
    // like
    val type: ArchComponentType,
    val port: List<Port>,
    val children: List<ArchComponent>
)

enum class ArchComponentType {
    SERVICE,
    MODULE,
    PACKAGE,
    CLASSES,
}

class Port(
    val protocols: List<Protocol>,
    // link HTTP API, RPC API, CLI API
    val inbounds: List<String>,
    // like database, HTTP Call
    val outbound: List<String>,
)

class Protocol(
    val name: String
)