package org.archguard.scanner.core.context

interface ContainerService

// context of the scanner runtime, hold the data and the client
interface Context {
    val language: String
    val features: List<String>
    val client: ArchGuardClient
}
