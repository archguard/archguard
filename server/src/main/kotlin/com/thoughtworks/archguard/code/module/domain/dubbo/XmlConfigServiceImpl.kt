package com.thoughtworks.archguard.code.module.domain.dubbo

import com.thoughtworks.archguard.code.module.domain.model.JClassVO
import org.archguard.protocol.dubbo.SubModuleDubbo
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class XmlConfigServiceImpl : XmlConfigService {
    private val log = LoggerFactory.getLogger(XmlConfigServiceImpl::class.java)

    @Autowired
    lateinit var dubboConfigRepository: DubboConfigRepository

    override fun getRealCalleeModuleByXmlConfig(systemId: Long, callerClass: JClassVO, calleeClass: JClassVO): List<SubModuleDubbo> {
        val callerModule = callerClass.module ?: throw RuntimeException("callerModule is null, is impossible")
        val callerSubModule = dubboConfigRepository.getSubModuleByName(systemId, callerModule) ?: return emptyList()
        val referenceConfigs = dubboConfigRepository.getReferenceConfigBy(systemId, calleeClass.name, callerSubModule)
        val serviceConfigs = referenceConfigs.map { referenceConfig -> dubboConfigRepository.getServiceConfigBy(systemId, referenceConfig) }.flatten()
        return serviceConfigs.map { it.subModule }
    }
}
