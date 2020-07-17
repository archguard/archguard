package com.thoughtworks.archguard.module.domain.dependency

import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JMethod
import com.thoughtworks.archguard.module.domain.model.JMethodVO

interface DependencyService {
    fun getLogicModulesDependencies(caller: String, callee: String): List<Dependency<JMethod>>
    fun getAll(): List<Dependency<JMethodVO>>
    fun getAllWithFilter(callerFilter: List<String>, calleeFilter: List<String>): List<Dependency<JMethodVO>>
}
