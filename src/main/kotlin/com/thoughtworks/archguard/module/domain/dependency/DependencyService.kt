package com.thoughtworks.archguard.module.domain.dependency

import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JMethod
import com.thoughtworks.archguard.module.domain.model.JMethodVO

interface DependencyService {
    fun getLogicModulesDependencies(caller: String, callee: String): List<Dependency<JMethod>>
    fun getAll(): List<Dependency<JMethodVO>>
    fun getAllWithFullNameRegex(callerRegex: List<Regex>, calleeRegex: List<Regex>): List<Dependency<JMethodVO>>
    fun getAllWithFullNameStart(callerStart: List<String>, calleeStart: List<String>): List<Dependency<JMethodVO>>
}
