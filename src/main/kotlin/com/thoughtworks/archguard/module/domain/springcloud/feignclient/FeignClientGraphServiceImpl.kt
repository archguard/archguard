package com.thoughtworks.archguard.module.domain.springcloud.feignclient

import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.module.domain.DefaultGraphService
import com.thoughtworks.archguard.module.domain.LogicModuleRepository
import com.thoughtworks.archguard.module.domain.dubbo.DubboGraphServiceImpl
import com.thoughtworks.archguard.module.domain.getModule
import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClass
import com.thoughtworks.archguard.module.domain.model.LogicModule
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
@Qualifier("FeignClient")
class FeignClientGraphServiceImpl(logicModuleRepository: LogicModuleRepository, jClassRepository: JClassRepository, val feignClientService: FeignClientService) : DefaultGraphService(logicModuleRepository, jClassRepository) {
    private val log = LoggerFactory.getLogger(FeignClientGraphServiceImpl::class.java)

    override fun mapClassDependencyToModuleDependency(logicModules: List<LogicModule>, jClassDependency: Dependency<JClass>): List<Dependency<LogicModule>> {
        val callerClass = jClassDependency.caller
        var calleeClass = jClassDependency.callee
        val feignClients = feignClientService.getFeignClients()

        val foundFeignClient = feignClients.filter { it.jClass == calleeClass }
        if (foundFeignClient.isNotEmpty()) {
            calleeClass = JClass(calleeClass.name, foundFeignClient[0].arg.name)
        }

        log.info(calleeClass.toString())

        val callerModules = getModule(logicModules, callerClass)
        val calleeModules = getModule(logicModules, calleeClass)


        return calleeModules.map { Dependency(callerModules[0], it) }
    }
}
