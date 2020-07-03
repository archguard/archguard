package com.thoughtworks.archguard.module.infrastructure

import com.thoughtworks.archguard.module.domain.Dependency
import com.thoughtworks.archguard.module.domain.JClass
import com.thoughtworks.archguard.module.domain.JMethod

class ModuleDependencyDto(var caller: String, var callerClass: String, var callerMethod: String, var callee: String, var calleeClass: String, var calleeMethod: String) {
    fun toMethodDependency(): Dependency<JMethod> {
        return Dependency(JMethod(callerMethod, JClass(callerClass, caller)), JMethod(calleeMethod, JClass(calleeClass, callee)))
    }

    companion object {
        fun fromJMethodDependency(jMethodDependency: Dependency<JMethod>): ModuleDependencyDto {
            return ModuleDependencyDto(jMethodDependency.caller.jClass.module, jMethodDependency.caller.jClass.name, jMethodDependency.caller.name,
                    jMethodDependency.callee.jClass.module, jMethodDependency.callee.jClass.name, jMethodDependency.caller.name)
        }
    }
}