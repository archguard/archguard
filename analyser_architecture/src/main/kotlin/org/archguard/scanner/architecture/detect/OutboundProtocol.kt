package org.archguard.scanner.architecture.detect

enum class OutboundProtocol {
    HTTP,
    RPC,
    DB;

    companion object {
        fun fromString(name: String): OutboundProtocol {
            return when (name.lowercase()) {
                "http" -> HTTP
                "rpc" -> RPC
                "db" -> DB
                else -> HTTP
            }
        }
    }
}