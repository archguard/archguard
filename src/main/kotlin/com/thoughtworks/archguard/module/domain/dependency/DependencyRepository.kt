package com.thoughtworks.archguard.module.domain.dependency

import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClassVO
import com.thoughtworks.archguard.module.domain.model.JMethodVO
import com.thoughtworks.archguard.module.domain.model.LogicComponent

interface DependencyRepository {
    fun getAllMethodDependencies(): List<Dependency<JMethodVO>>
    fun getAllClassDependencyLegacy(members: List<LogicComponent>): List<Dependency<JClassVO>>

}
