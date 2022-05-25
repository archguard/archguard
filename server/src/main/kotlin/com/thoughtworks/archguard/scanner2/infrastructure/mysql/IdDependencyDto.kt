package com.thoughtworks.archguard.scanner2.infrastructure.mysql

import com.thoughtworks.archguard.scanner2.domain.model.Dependency

data class IdDependencyDto(val caller: String, val callee: String) {
    fun toDependency(): Dependency<String> {
        return Dependency(caller, callee)
    }
}
