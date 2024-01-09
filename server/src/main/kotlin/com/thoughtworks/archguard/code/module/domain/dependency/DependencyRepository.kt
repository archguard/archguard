package com.thoughtworks.archguard.code.module.domain.dependency

import com.thoughtworks.archguard.code.module.domain.model.JMethodVO
import org.archguard.model.Dependency

interface DependencyRepository {
    fun getAllMethodDependencies(systemId: Long): List<Dependency<JMethodVO>>
}
