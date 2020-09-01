package com.thoughtworks.archguard.module.domain.dubbo

import com.thoughtworks.archguard.module.domain.model.JClassVO
import org.springframework.stereotype.Service

@Service
interface XmlConfigService {
    fun getRealCalleeModuleByXmlConfig(systemId: Long, callerClass: JClassVO, calleeClass: JClassVO): List<SubModuleDubbo>
}
