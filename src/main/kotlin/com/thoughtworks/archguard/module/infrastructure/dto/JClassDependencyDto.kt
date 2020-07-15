package com.thoughtworks.archguard.module.infrastructure.dto

import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClassVO

class JClassDependencyDto(val moduleCaller: String, val classCaller: String, val moduleCallee: String, val classCallee: String) {
    fun toJClassDependency(): Dependency<JClassVO> {
        return Dependency(JClassVO(classCaller, moduleCaller), JClassVO(classCallee, moduleCallee))
    }
}