package com.thoughtworks.archguard.code.module.domain.dependency

import com.thoughtworks.archguard.code.module.domain.model.Dependency
import com.thoughtworks.archguard.code.module.domain.model.JMethodVO

interface DependencyRepository {
    fun getAllMethodDependencies(systemId: Long): List<Dependency<JMethodVO>>
}
