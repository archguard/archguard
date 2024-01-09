package com.thoughtworks.archguard.code.clazz.domain.service

import com.thoughtworks.archguard.code.clazz.domain.JClassRepository
import com.thoughtworks.archguard.code.method.domain.JMethodRepository
import com.thoughtworks.archguard.code.method.domain.service.MethodCalleesService
import com.thoughtworks.archguard.code.method.domain.service.MethodConfigService
import com.thoughtworks.archguard.config.domain.ConfigureService
import org.archguard.model.code.JClass
import org.springframework.stereotype.Service

@Service
class ClassMethodCalleesService(
    val methodRepo: JMethodRepository,
    val classRepo: JClassRepository,
    val methodCalleesService: MethodCalleesService,
    val configureService: ConfigureService,
    val classConfigService: ClassConfigService,
    val methodConfigService: MethodConfigService
) {

    fun findClassMethodsCallees(
        systemId: Long,
        target: JClass,
        calleeDeep: Int,
        needIncludeImpl: Boolean,
        needParents: Boolean
    ): JClass {
        if (target.module == null) {
            return target
        }
        val methods = methodRepo.findMethodsByModuleAndClass(systemId, target.module, target.name).filter {
            configureService.isDisplayNode(systemId, it.name) && configureService.isDisplayNode(
                systemId,
                it.clazz
            )
        }
        methodConfigService.buildColorConfig(methods, systemId)
        target.methods = methods

        methodCalleesService.buildMethodCallees(systemId, target.methods, calleeDeep, needIncludeImpl)
        if (needParents) {
            val parents = classRepo.findClassParents(systemId, target.module!!, target.name)
                .filter { configureService.isDisplayNode(systemId, it.name) }
            classConfigService.buildJClassColorConfig(parents, systemId)
            (target.parents as MutableList).addAll(parents)
            target.parents.forEach { findClassMethodsCallees(systemId, it, calleeDeep, needIncludeImpl, needParents) }
        }
        return target
    }
}
