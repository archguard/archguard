package com.thoughtworks.archguard.architecture.domain.model

class Architecture private constructor(val archSystemId: String) {
    var style: ArchStyle? = null
    var components: List<ArchComponent> = ArrayList()
    var connections: List<ArchComponentConnection> = ArrayList()

    enum class ArchStyle {
        LAYERED
    }

    companion object {
        fun build(archSystemId: String): Architecture {
            return Architecture(archSystemId)
        }
    }
}
