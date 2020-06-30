package com.thoughtworks.archguard.module.infrastructure

import com.thoughtworks.archguard.module.domain.DubboConfigRepository
import com.thoughtworks.archguard.module.domain.JClass
import com.thoughtworks.archguard.module.domain.LogicModule
import com.thoughtworks.archguard.module.domain.SubModule
import com.thoughtworks.archguard.module.domain.XmlConfigService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class XmlConfigServiceImpl : XmlConfigService {
    private val log = LoggerFactory.getLogger(XmlConfigServiceImpl::class.java)

    @Autowired
    lateinit var dubboConfigRepository: DubboConfigRepository


    override fun getModuleByDependency(callerClass: JClass, calleeClass: JClass, modules: List<LogicModule>): List<SubModule> {
        val callerModule = callerClass.module
        val callerSubModule = dubboConfigRepository.getModuleBy(callerModule)
        val referenceConfigs = dubboConfigRepository.getReferenceConfigBy(callerClass.name, callerSubModule)
        val serviceConfigs = referenceConfigs.map { referenceConfig -> dubboConfigRepository.getServiceConfigBy(referenceConfig) }.flatten()
        return serviceConfigs.map { it.subModule }
    }
}