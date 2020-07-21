package com.thoughtworks.archguard.module.infrastructure.dto

import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClassVO
import com.thoughtworks.archguard.module.domain.model.JMethodLegacy

class MethodDependencyDto(var caller: String, var callerClass: String, var callerMethod: String, var callee: String, var calleeClass: String, var calleeMethod: String) {
    fun toMethodDependency(): Dependency<JMethodLegacy> {
        return Dependency(JMethodLegacy(callerMethod, JClassVO(callerClass, caller)), JMethodLegacy(calleeMethod, JClassVO(calleeClass, callee)))
    }

    companion object {
        fun fromJMethodDependency(jMethodLegacyDependency: Dependency<JMethodLegacy>): MethodDependencyDto {
            return MethodDependencyDto(jMethodLegacyDependency.caller.jClass.module, jMethodLegacyDependency.caller.jClass.name, jMethodLegacyDependency.caller.name,
                    jMethodLegacyDependency.callee.jClass.module, jMethodLegacyDependency.callee.jClass.name, jMethodLegacyDependency.callee.name)
        }
    }
}