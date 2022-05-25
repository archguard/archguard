package com.thoughtworks.archguard.code.method.domain.service

import com.thoughtworks.archguard.code.method.domain.JMethod
import com.thoughtworks.archguard.config.domain.ConfigureService
import org.springframework.stereotype.Service

@Service
class MethodConfigService(val configureService: ConfigureService) {

    fun buildColorConfig(jMethods: List<JMethod>, systemId: Long) {
        // 当方法名包含配置key时，着色配置生效
        jMethods.forEach { jMethod -> jMethod.buildColorConfigure(configureService.getNodeRelatedColorConfigures(systemId, jMethod.name)) }
    }
}
