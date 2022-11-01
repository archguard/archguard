package com.thoughtworks.archguard.architecture.domain.model

import java.util.UUID

class ArchSystem private constructor(val id: String) {
    var name: String? = null
    var architecture: Architecture? = null

    companion object {
        fun build(): ArchSystem {
            val id = UUID.randomUUID().toString()
            return ArchSystem(id)
        }
    }
}
