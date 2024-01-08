package org.archguard.wrapper

import org.archguard.model.Dependency
import org.archguard.model.code.JClass
import org.archguard.model.code.JMethod

interface CircularDependencyServiceInterface {
    fun getMethodsHasModules(systemId: Long): List<JMethod>
    fun getJClassesHasModules(systemId: Long): List<JClass>
    fun getAllClassIdDependencies(systemId: Long): List<Dependency<String>>
    fun getAllMethodDependencies(systemId: Long): List<Dependency<String>>
}