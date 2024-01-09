package com.thoughtworks.archguard.code.module.domain.dependency

import org.archguard.model.Dependency
import org.archguard.model.vos.JMethodVO

interface DependencyRepository {
    fun getAllMethodDependencies(systemId: Long): List<Dependency<JMethodVO>>
}
