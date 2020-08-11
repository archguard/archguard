package com.thoughtworks.archguard.clazz.domain.service

import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.config.domain.ConfigureService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ClassInvokeService {
    private val log = LoggerFactory.getLogger(ClassInvokeService::class.java)

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
        if (target.module == "null") {
            return
        }
        var implements = listOf<JClass>()
        if (needIncludeImpl) {
            implements = repo.findClassImplements(target.name, target.module)
                    .filter { configureService.isDisplayNode(it.name) }
        }
        target.implements = implements
        target.callees = repo.findCallees(target.name, target.module)
                .filter { configureService.isDisplayNode(it.clazz.name) }
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
        if (target.module == "null") {
            return
        }
        target.parents = repo.findClassParents(target.module, target.name)
                .filter { configureService.isDisplayNode(it.name) }
        target.callers = repo.findCallers(target.name, target.module)
                .filter { configureService.isDisplayNode(it.clazz.name) }
        if (deep == 1) {
            return
        } else {
            target.parents.map { findClassCallers(it, deep - 1, needIncludeImpl) }
            target.callers.map { findClassCallers(it.clazz, deep - 1, needIncludeImpl) }
        }
    }


}
