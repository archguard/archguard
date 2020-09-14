package com.thoughtworks.archgard.scanner2.infrastructure.mysql

import com.thoughtworks.archgard.scanner2.domain.service.Dependency

data class IdDependencyDto(val caller: String, val callee: String) {
    fun toDependency(): Dependency<String> {
        return Dependency(caller, callee)
    }
}