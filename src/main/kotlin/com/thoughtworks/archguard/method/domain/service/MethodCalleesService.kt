package com.thoughtworks.archguard.method.domain.service

import com.thoughtworks.archguard.method.domain.JMethod
import com.thoughtworks.archguard.method.domain.JMethodRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MethodCalleesService {
    @Autowired
    private lateinit var repo: JMethodRepository

    fun buildMethodCallees(methods: List<JMethod>, calleeDeep: Int, needIncludeImpl: Boolean): List<JMethod> {
        val container = ArrayList<JMethod>()
        doBuildCallees(methods, calleeDeep, container, needIncludeImpl)
        return methods
    }

    private fun doBuildCallees(methods: List<JMethod>, calleeDeep: Int, container: MutableList<JMethod>, needIncludeImpl: Boolean) {
        val pendindMethods = methods.filterNot { container.contains(it) }
        if (pendindMethods.isEmpty() || calleeDeep == 0) {
            container.addAll(pendindMethods)
        } else {
            pendindMethods.forEach {
                it.callees = repo.findMethodCallees(it.id)
                if (needIncludeImpl) {
                    it.implements = repo.findMethodImplements(it.id, it.name)
                }
            }
            doBuildCallees(pendindMethods.flatMap { it.callees },
                    calleeDeep - 1, container, needIncludeImpl)
            doBuildCallees(pendindMethods.flatMap { it.implements },
                    calleeDeep, container, needIncludeImpl)
        }
    }

    fun findCallees(target: JMethod, deep: Int, needIncludeImpl: Boolean): JMethod {
        return buildMethodCallees(listOf(target), deep, needIncludeImpl)[0]
    }
}
