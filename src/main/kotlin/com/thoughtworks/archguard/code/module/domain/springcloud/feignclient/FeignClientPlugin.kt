package com.thoughtworks.archguard.code.module.domain.springcloud.feignclient

import com.thoughtworks.archguard.code.module.domain.model.Dependency
import com.thoughtworks.archguard.code.module.domain.model.JMethodVO
import com.thoughtworks.archguard.code.module.domain.plugin.AbstractDependPlugin
import com.thoughtworks.archguard.code.module.domain.plugin.PluginType
import com.thoughtworks.archguard.code.module.domain.springcloud.SpringCloudServiceRepository
import com.thoughtworks.archguard.code.module.domain.springcloud.httprequest.HttpRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class FeignClientPlugin: AbstractDependPlugin() {
    @Autowired
    lateinit var feignClientService: FeignClientService

    @Autowired
    lateinit var springCloudServiceRepository: SpringCloudServiceRepository

    override fun getPluginType(): PluginType {
        return PluginType.FEIGN_CLIENT
    }

    override fun fixMethodDependencies(systemId: Long, methodDependencies: List<Dependency<JMethodVO>>): List<Dependency<JMethodVO>>{
        return methodDependencies + feignClientService.getFeignClientMethodDependencies().map { Dependency(mapHttpRequestToMethod(it.caller), mapHttpRequestToMethod(it.callee)) }
    }

    private fun mapHttpRequestToMethod(httpRequest: HttpRequest): JMethodVO{
        return springCloudServiceRepository.getMethodById(httpRequest.targetId)
    }
}
