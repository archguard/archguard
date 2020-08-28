package com.thoughtworks.archguard.clazz.domain.service

import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.clazz.exception.ClassNotFountException
import org.springframework.stereotype.Service

@Service
class ClassService(val classMethodCalleesService: ClassMethodCalleesService, val classDependenceesService: ClassDependenceesService,
                   val classDependencerService: ClassDependencerService, val jClassRepository: JClassRepository, val classInvokeService: ClassInvokeService) {

    fun getDependencies(projectId: Long, module: String, name: String, deep: Int): JClass {
        val target = getTargetClass(projectId, module, name)
        classDependenceesService.findDependencees(target, deep)
        classDependencerService.findDependencers(target, deep)
        return target
    }

    private fun getTargetClass(projectId: Long, module: String, name: String): JClass {
        return jClassRepository.getJClassBy(projectId, name, module)
                ?: throw ClassNotFountException("Can't find class by module:${module}, class:${name}")
    }

    fun findInvokes(projectId: Long, module: String, name: String, callerDeep: Int, calleeDeep: Int, needIncludeImpl: Boolean): JClass {
        val targetClass = getTargetClass(projectId, module, name)
        return classInvokeService.findInvokes(projectId, targetClass, callerDeep, calleeDeep, needIncludeImpl)
    }

    fun findMethodsCallees(projectId: Long, module: String, name: String, calleeDeep: Int,
                           needIncludeImpl: Boolean, needParents: Boolean): JClass {
        val targetClass = getTargetClass(projectId, module, name)
        return classMethodCalleesService.findClassMethodsCallees(projectId, targetClass,
                calleeDeep, needIncludeImpl, needParents)
    }
}
