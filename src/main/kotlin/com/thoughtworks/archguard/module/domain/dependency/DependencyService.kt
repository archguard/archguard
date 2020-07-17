package com.thoughtworks.archguard.module.domain.dependency

import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JMethod

interface DependencyService {
    fun getLogicModulesDependencies(caller: String, callee: String): List<Dependency<JMethod>>
}
