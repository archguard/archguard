package org.archguard.architecture.detect

enum class AppType {
    Web,
    Compiler,
    Cli,
    Data;

    companion object {
        fun fromString(name: String): AppType {
            return when (name.lowercase()) {
                "web" -> Web
                "compiler" -> Compiler
                "cli" -> Cli
                "data" -> Data
                else -> throw Exception("unknown app type: $name")
            }
        }
    }
}