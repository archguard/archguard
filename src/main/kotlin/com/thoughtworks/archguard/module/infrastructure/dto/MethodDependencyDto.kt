package com.thoughtworks.archguard.module.infrastructure.dto

import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClass
import com.thoughtworks.archguard.module.domain.model.JClassVO
import com.thoughtworks.archguard.module.domain.model.JMethod

class MethodDependencyDto(var caller: String, var callerClass: String, var callerMethod: String, var callee: String, var calleeClass: String, var calleeMethod: String) {
    fun toMethodDependency(): Dependency<JMethod> {
        return Dependency(JMethod(callerMethod, JClassVO(callerClass, caller)), JMethod(calleeMethod, JClassVO(calleeClass, callee)))
    }

    companion object {
        fun fromJMethodDependency(jMethodDependency: Dependency<JMethod>): MethodDependencyDto {
            return MethodDependencyDto(jMethodDependency.caller.jClass.module, jMethodDependency.caller.jClass.name, jMethodDependency.caller.name,
                    jMethodDependency.callee.jClass.module, jMethodDependency.callee.jClass.name, jMethodDependency.callee.name)
        }
    }
}