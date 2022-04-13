package com.thoughtworks.archguard.code.module.domain.springcloud.feignclient

import com.thoughtworks.archguard.code.module.domain.JAnnotationRepository
import com.thoughtworks.archguard.code.module.domain.model.Dependency
import com.thoughtworks.archguard.code.module.domain.springcloud.SpringCloudServiceRepository
import com.thoughtworks.archguard.code.module.domain.springcloud.httprequest.HttpRequest
import com.thoughtworks.archguard.code.module.domain.springcloud.httprequest.HttpRequestService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.lang.annotation.ElementType

@Service
class FeignClientService(val jAnnotationRepository: JAnnotationRepository, val springCloudServiceRepository: SpringCloudServiceRepository, val httpRequestService: HttpRequestService) {

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
            val serviceName = springCloudServiceRepository.getServiceNameByMethodId(it.targetId)
            services[serviceName]?.add(it) ?: services.put(serviceName, mutableListOf(it))
        }

        feignClients.forEach {
            val serviceName = it.arg.name
            val methods = springCloudServiceRepository.getMethodIdsByClassId(it.targetId)
            val callers = httpRequestMethods.filter { method -> method.targetId in methods }.map { method -> margeFeignClientArgToMethod(it.arg, method) }
            val callees = services.getOrDefault(serviceName, mutableListOf())

            feignClientMethodDependencies.addAll(callers.flatMap { caller -> mapToMethod(caller, callees).map { callee -> Dependency(caller, callee) } })

        }

        return feignClientMethodDependencies

    }

    private fun margeFeignClientArgToMethod(feignClientArg: FeignClientArg, method: HttpRequest): HttpRequest {
        return method.apply { arg.paths = arg.paths.map { feignClientArg.path + it } }
    }

    private fun mapToMethod(caller: HttpRequest, callees: List<HttpRequest>): List<HttpRequest> {
        return callees.filter { it.arg.paths.any { calleePath -> caller.arg.paths.any { callerPath -> matchPath(callerPath, calleePath) } } && it.arg.methods.intersect(caller.arg.methods).isNotEmpty() }
    }

    private fun matchPath(callerPath: String, calleePath: String): Boolean {
        val regex = Regex("""\{[^/]*}""")
        val callerPathStr = callerPath.replace(regex, "any")
        val calleePathRegex = calleePath.replace(regex, """[^/]*""")
        
        return Regex(calleePathRegex).matches(callerPathStr)
    }


}
