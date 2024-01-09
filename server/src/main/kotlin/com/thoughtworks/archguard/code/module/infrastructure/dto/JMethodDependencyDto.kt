package com.thoughtworks.archguard.code.module.infrastructure.dto

import org.archguard.model.Dependency
import org.archguard.model.vos.JMethodVO

data class JMethodDependencyDto(val callerModule: String, val callerClass: String, val callerMethod: String, val callerReturnType: String, val callerArgumentTypes: String, val calleeModule: String, val calleeClass: String, val calleeMethod: String, val calleeReturnType: String, val calleeArgumentTypes: String) {
    fun toJMethodDependency(): Dependency<JMethodVO> {
        return Dependency(
            JMethodVO(
                callerMethod,
                callerClass,
                callerModule,
                callerReturnType,
                callerArgumentTypes.split(",")
            ),
            JMethodVO(
                calleeMethod,
                calleeClass,
                calleeModule,
                calleeReturnType,
                calleeArgumentTypes.split(",")
            )
        )
    }
}
