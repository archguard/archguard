package com.thoughtworks.archguard.code.module.infrastructure.dto

import com.thoughtworks.archguard.code.module.domain.model.JClassVO
import org.archguard.model.Dependency

class JClassDependencyDto(val moduleCaller: String, val classCaller: String, val moduleCallee: String, val classCallee: String) {
    fun toJClassDependency(): Dependency<JClassVO> {
        return Dependency(JClassVO(classCaller, moduleCaller), JClassVO(classCallee, moduleCallee))
    }
}
