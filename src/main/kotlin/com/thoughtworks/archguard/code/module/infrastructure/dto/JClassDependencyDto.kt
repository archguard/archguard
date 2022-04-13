package com.thoughtworks.archguard.code.module.infrastructure.dto

import com.thoughtworks.archguard.code.module.domain.model.Dependency
import com.thoughtworks.archguard.code.module.domain.model.JClassVO

class JClassDependencyDto(val moduleCaller: String, val classCaller: String, val moduleCallee: String, val classCallee: String) {
    fun toJClassDependency(): Dependency<JClassVO> {
        return Dependency(JClassVO(classCaller, moduleCaller), JClassVO(classCallee, moduleCallee))
    }
}