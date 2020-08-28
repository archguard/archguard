package com.thoughtworks.archguard.module.domain.dubbo

import com.thoughtworks.archguard.module.domain.model.JClassVO
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class XmlConfigServiceImpl : XmlConfigService {
    private val log = LoggerFactory.getLogger(XmlConfigServiceImpl::class.java)

    @Autowired
    lateinit var dubboConfigRepository: DubboConfigRepository


    override fun getRealCalleeModuleByXmlConfig(projectId: Long, callerClass: JClassVO, calleeClass: JClassVO): List<SubModuleDubbo> {
        val callerModule = callerClass.module
        val callerSubModule = dubboConfigRepository.getSubModuleByName(projectId, callerModule) ?: return emptyList()
        val referenceConfigs = dubboConfigRepository.getReferenceConfigBy(projectId, calleeClass.name, callerSubModule)
        val serviceConfigs = referenceConfigs.map { referenceConfig -> dubboConfigRepository.getServiceConfigBy(projectId, referenceConfig) }.flatten()
        return serviceConfigs.map { it.subModule }
    }
}
