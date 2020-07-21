package com.thoughtworks.archguard.method.domain.service

import com.thoughtworks.archguard.method.domain.JMethod
import org.springframework.stereotype.Service

@Service
class MethodCalleesService {
    fun buildMethodCallees(jMethod: JMethod, calleeDeep: Int, needIncludeImpl: Boolean, needParents: Boolean): JMethod {
        TODO()
    }

}
