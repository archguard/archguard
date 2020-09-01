package com.thoughtworks.archguard.clazz.domain.service

import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.clazz.exception.ClassNotFountException
import org.springframework.stereotype.Service

@Service
class ClassService(val classMethodCalleesService: ClassMethodCalleesService, val classDependenceesService: ClassDependenceesService,
                   val classDependencerService: ClassDependencerService, val jClassRepository: JClassRepository,
                   val classInvokeService: ClassInvokeService) {

    fun getDependencies(systemId: Long, module: String, name: String, deep: Int): JClass {
        val target = getTargetClass(systemId, module, name)
        classDependenceesService.findDependencees(systemId, target, deep)
        classDependencerService.findDependencers(systemId, target, deep)
        return target
    }

    private fun getTargetClass(systemId: Long, module: String, name: String): JClass {
        return jClassRepository.getJClassBy(systemId, name, module)
                ?: throw ClassNotFountException("Can't find class by module:${module}, class:${name}")
    }

    fun findInvokes(systemId: Long, module: String, name: String, callerDeep: Int, calleeDeep: Int, needIncludeImpl: Boolean): JClass {
        val targetClass = getTargetClass(systemId, module, name)
        return classInvokeService.findInvokes(systemId, targetClass, callerDeep, calleeDeep, needIncludeImpl)
    }

    fun findMethodsCallees(systemId: Long, module: String, name: String, calleeDeep: Int,
                           needIncludeImpl: Boolean, needParents: Boolean): JClass {
        val targetClass = getTargetClass(systemId, module, name)
        return classMethodCalleesService.findClassMethodsCallees(systemId, targetClass,
                calleeDeep, needIncludeImpl, needParents)
    }
}
