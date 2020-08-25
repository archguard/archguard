package com.thoughtworks.archguard.module.domain.dependency

import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClassVO
import com.thoughtworks.archguard.module.domain.model.JMethodVO

interface DependencyService {
    fun getAllMethodDependencies(projectId: Long): List<Dependency<JMethodVO>>
    fun getAllWithFullNameStart(projectId: Long, callerStart: List<String>, calleeStart: List<String>): List<Dependency<JMethodVO>>
    fun getAllClassDependencies(projectId: Long): List<Dependency<JClassVO>>
    fun getAllMethodDependencies(projectId: Long, caller: String, callee: String): List<Dependency<JMethodVO>>
}
