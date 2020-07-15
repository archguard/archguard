package com.thoughtworks.archguard.module.domain.springcloud.feignclient

import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.module.domain.JAnnotationRepository
import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.springcloud.httprequest.HttpRequest
import com.thoughtworks.archguard.module.domain.springcloud.httprequest.HttpRequestService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.lang.annotation.ElementType

@Service
class FeignClientService(val jAnnotationRepository: JAnnotationRepository, val httpRequestService: HttpRequestService, val jClassRepository: JClassRepository, val feignClientRepository: FeignClientRepository) {

    private val log = LoggerFactory.getLogger(FeignClientService::class.java)


    fun getFeignClients(): List<FeignClient> {
        val feignClientAnnotations = jAnnotationRepository.getJAnnotationWithValueByName("feign.FeignClient").filter { it.targetType == ElementType.TYPE.name }
        return feignClientAnnotations.map { FeignClient(it.targetId, FeignClientArg(it.values.orEmpty())) }
    }

    fun getFeignClientMethodDependencies(): List<Dependency<HttpRequest>> {
        val feignClientMethodDependencies = mutableListOf<Dependency<HttpRequest>>()

        val feignClients = getFeignClients()
        val httpRequestMethods = httpRequestService.getHttpRequests()
        
        val services = mutableMapOf<String, MutableList<HttpRequest>>()
        httpRequestMethods.forEach {
            val serviceName = feignClientRepository.getServiceNameByMethodId(it.targetId)
            services[serviceName]?.add(it) ?: services.put(serviceName, mutableListOf(it))
        }
        
        feignClients.forEach {
            val serviceName = it.arg.name
            val methods = jClassRepository.getMethodsById(it.targetId)
            val callers = httpRequestMethods.filter { method -> method.targetId in methods }
            val callees = services.getOrDefault(serviceName, mutableListOf())

            feignClientMethodDependencies.addAll(callers.flatMap { caller -> mapToMethod(caller, callees).map { callee -> Dependency(caller, callee) } })

        }

        return feignClientMethodDependencies

    }

    private fun mapToMethod(caller: HttpRequest, callees: List<HttpRequest>): List<HttpRequest>{
        return callees.filter { it.arg.path.intersect(caller.arg.path).isNotEmpty() && it.arg.method.intersect(caller.arg.method).isNotEmpty() }
    }





}
