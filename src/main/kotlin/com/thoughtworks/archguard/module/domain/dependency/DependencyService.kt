package com.thoughtworks.archguard.module.domain.dependency

import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClassVO
import com.thoughtworks.archguard.module.domain.model.JMethodVO

interface DependencyService {
    fun getAllMethodDependencies(): List<Dependency<JMethodVO>>
    fun getAllWithFullNameStart(callerStart: List<String>, calleeStart: List<String>): List<Dependency<JMethodVO>>
    fun getAllClassDependencies(): List<Dependency<JClassVO>>
}
