package com.thoughtworks.archguard.module.domain.springcloud.feignclient

import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JMethodVO
import com.thoughtworks.archguard.module.domain.plugin.Plugin
import com.thoughtworks.archguard.module.domain.springcloud.SpringCloudServiceRepository
import com.thoughtworks.archguard.module.domain.springcloud.httprequest.HttpRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component("FeignClientPlugin")
class FeignClientPlugin: Plugin() {
    @Autowired
    lateinit var feignClientService: FeignClientService

    @Autowired
    lateinit var springCloudServiceRepository: SpringCloudServiceRepository

    override fun fixMethodDependencies(methodDependencies: List<Dependency<JMethodVO>>): List<Dependency<JMethodVO>>{
        return methodDependencies + feignClientService.getFeignClientMethodDependencies().map { Dependency(mapHttpRequestToMethod(it.caller), mapHttpRequestToMethod(it.callee)) }
    }

    private fun mapHttpRequestToMethod(httpRequest: HttpRequest): JMethodVO{
        return springCloudServiceRepository.getMethodById(httpRequest.targetId)
    }
}
