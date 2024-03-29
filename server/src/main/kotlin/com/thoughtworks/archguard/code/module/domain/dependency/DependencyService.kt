package com.thoughtworks.archguard.code.module.domain.dependency

import org.archguard.model.Dependency
import org.archguard.model.vos.JClassVO
import org.archguard.model.vos.JMethodVO

interface DependencyService {
    fun getAllMethodDependencies(systemId: Long): List<Dependency<JMethodVO>>
    fun getAllWithFullNameStart(systemId: Long, callerStart: List<String>, calleeStart: List<String>): List<Dependency<JMethodVO>>
    fun getAllClassDependencies(systemId: Long): List<Dependency<JClassVO>>
    fun getAllMethodDependencies(systemId: Long, caller: String, callee: String): List<Dependency<JMethodVO>>
    fun getAllDistinctMethodDependencies(systemId: Long, caller: String, callee: String): List<Dependency<JMethodVO>>
}
