package com.thoughtworks.archguard.code.module.domain.dubbo

import org.archguard.model.vos.JClassVO
import org.archguard.protocol.dubbo.SubModuleDubbo
import org.springframework.stereotype.Service

@Service
interface XmlConfigService {
    fun getRealCalleeModuleByXmlConfig(
        systemId: Long,
        callerClass: JClassVO,
        calleeClass: JClassVO
    ): List<SubModuleDubbo>
}
