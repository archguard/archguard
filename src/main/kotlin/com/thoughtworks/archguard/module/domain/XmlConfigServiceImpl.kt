package com.thoughtworks.archguard.module.domain

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class XmlConfigServiceImpl : XmlConfigService {
    private val log = LoggerFactory.getLogger(XmlConfigServiceImpl::class.java)

    @Autowired
    lateinit var dubboConfigRepository: DubboConfigRepository


    override fun getRealCalleeModuleByDependency(callerClass: JClass, calleeClass: JClass): List<SubModule> {
        val callerModule = callerClass.module
        val callerSubModule = dubboConfigRepository.getModuleByName(callerModule)
        val referenceConfigs = dubboConfigRepository.getReferenceConfigBy(callerClass.name, callerSubModule)
        val serviceConfigs = referenceConfigs.map { referenceConfig -> dubboConfigRepository.getServiceConfigBy(referenceConfig) }.flatten()
        return serviceConfigs.map { it.subModule }
    }
}