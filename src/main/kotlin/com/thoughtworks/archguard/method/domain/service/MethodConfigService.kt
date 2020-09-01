package com.thoughtworks.archguard.method.domain.service

import com.thoughtworks.archguard.config.domain.ConfigureService
import com.thoughtworks.archguard.method.domain.JMethod
import org.springframework.stereotype.Service

@Service
class MethodConfigService(val configureService: ConfigureService) {

    fun buildColorConfig(jMethods: List<JMethod>, projectId: Long) {
        // 当方法名包含配置key时，着色配置生效
        jMethods.forEach { jMethod -> jMethod.buildColorConfigure(configureService.getNodeRelatedColorConfigures(projectId, jMethod.name)) }
    }
}