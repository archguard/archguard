package com.thoughtworks.archguard.clazz.domain.service

import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.config.domain.ConfigureService
import com.thoughtworks.archguard.clazz.domain.JClass
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ClassInvokeService {
    @Autowired
    private lateinit var repo: JClassRepository
    @Autowired
    private lateinit var configureService: ConfigureService
    fun findInvokes(target: JClass, callerDeep: Int, calleeDeep: Int,
                    needIncludeImpl: Boolean): JClass {
        findClassCallers(target, callerDeep, needIncludeImpl)
        findClassCallees(target, calleeDeep, needIncludeImpl)
        return target
    }

    private fun findClassCallees(target: JClass, deep: Int, needIncludeImpl: Boolean) {
        if (deep == 0) {
            return
        }
        var implements = listOf<JClass>()
        if (needIncludeImpl) {
            implements = repo.findClassImplements(target.name, target.module)
                    .filter { configureService.isDisplayClass(it.name) }
        }
        target.implements = implements
        target.callees = repo.findCallees(target.name, target.module)
                .filter { configureService.isDisplayClass(it.clazz.name) }
        if (deep == 1) {
            return
        } else {
            target.implements.map { findClassCallees(it, deep - 1, needIncludeImpl) }
            target.callees.map { findClassCallees(it.clazz, deep - 1, needIncludeImpl) }
        }
    }

    private fun findClassCallers(target: JClass, deep: Int, needIncludeImpl: Boolean) {
        if (deep == 0) {
            return
        }
        target.parents = repo.findClassParents(target.module, target.name)
                .filter { configureService.isDisplayClass(it.name) }
        target.callers = repo.findCallers(target.name, target.module)
                .filter { configureService.isDisplayClass(it.clazz.name) }
        if (deep == 1) {
            return
        } else {
            target.parents.map { findClassCallers(it, deep - 1, needIncludeImpl) }
            target.callers.map { findClassCallers(it.clazz, deep - 1, needIncludeImpl) }
        }
    }


}
