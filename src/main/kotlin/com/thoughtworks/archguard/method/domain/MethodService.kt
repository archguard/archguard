package com.thoughtworks.archguard.method.domain

import com.thoughtworks.archguard.config.domain.ConfigureService
import com.thoughtworks.archguard.method.domain.service.MethodCalleesService
import com.thoughtworks.archguard.method.domain.service.MethodCallersService
import org.springframework.stereotype.Service

@Service
class MethodService(val repo: JMethodRepository, val calleeService: MethodCalleesService,
                    val callerService: MethodCallersService, val configureService: ConfigureService) {
    fun findMethodCallees(projectId: Long, moduleName: String, clazzName: String, methodName: String, deep: Int, needIncludeImpl: Boolean): List<JMethod> {
        val target = getMethodBy(projectId, moduleName, clazzName, methodName).filter { configureService.isDisplayNode(projectId, it.name) }
        calleeService.findCallees(projectId, target, deep, needIncludeImpl)
        return target
    }

    fun findMethodCallers(projectId: Long, moduleName: String, clazzName: String, methodName: String, deep: Int): List<JMethod> {
        val target = getMethodBy(projectId, moduleName, clazzName, methodName).filter { configureService.isDisplayNode(projectId, it.name) }
        callerService.findCallers(projectId, target, deep)
        return target
    }


    fun findMethodInvokes(projectId: Long, moduleName: String, clazzName: String, methodName: String,
                          callerDeep: Int, calleeDeep: Int, needIncludeImpl: Boolean): List<JMethod> {
        val target = getMethodBy(projectId, moduleName, clazzName, methodName).filter { configureService.isDisplayNode(projectId, it.name) }
        callerService.findCallers(projectId, target, callerDeep)
        calleeService.findCallees(projectId, target, calleeDeep, needIncludeImpl)
        return target
    }

    private fun getMethodBy(projectId: Long, moduleName: String, clazzName: String, methodName: String): List<JMethod> {
        return repo.findMethodByModuleAndClazzAndName(projectId, moduleName, clazzName, methodName).filter { configureService.isDisplayNode(projectId, it.name) }
    }

    fun findMethodByModuleAndClazz(projectId: Long, clazzName: String, submoduleName: String): List<JMethod> {
        return repo.findMethodsByModuleAndClass(projectId, submoduleName, clazzName)
    }

}
