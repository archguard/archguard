package com.thoughtworks.archguard.module.domain.dependency

import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClassVO
import com.thoughtworks.archguard.module.domain.model.JMethodLegacy
import com.thoughtworks.archguard.module.domain.model.JMethodVO
import com.thoughtworks.archguard.module.domain.model.LogicComponent

interface DependencyService {
    fun getAllMethodDependencies(): List<Dependency<JMethodVO>>
    fun getAllWithFullNameStart(callerStart: List<String>, calleeStart: List<String>): List<Dependency<JMethodVO>>
    fun getAllClassDependencies(): List<Dependency<JClassVO>>
    fun getAllClassDependencyLegacy(members: List<LogicComponent>): List<Dependency<JClassVO>>
}
