package com.thoughtworks.archguard.module.infrastructure.dto

import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JMethodVO

data class JMethodDependencyDto(val callerModule: String, val callerClass: String, val callerMethod: String, val calleeModule: String, val calleeClass: String, val calleeMethod: String) {
    fun toJMethodDependency(): Dependency<JMethodVO> {
        return Dependency(JMethodVO(callerModule, callerClass, calleeMethod), JMethodVO(calleeModule, calleeClass, calleeMethod))
    }
}
