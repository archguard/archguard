package com.thoughtworks.archguard.module.domain.springcloud.feignclient

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.thoughtworks.archguard.module.domain.JAnnotationRepository
import com.thoughtworks.archguard.module.domain.JClassRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.annotation.ElementType

@Service
class FeignClientService {
    @Autowired
    lateinit var jAnnotationRepository: JAnnotationRepository

    @Autowired
    lateinit var JClassRepository: JClassRepository

    var objectMapper = ObjectMapper()

    fun getFeignClients(): List<FeignClient> {
        val feignClientAnnotations = jAnnotationRepository.getJAnnotationWithValueByName("feign.FeignClient").filter { it.targetType == ElementType.TYPE.name }

        val feignClients = ArrayList<FeignClient>()
        feignClientAnnotations.forEach { it ->
            val jClass = JClassRepository.getJClassById(it.targetId)
            val name = it.values?.get("name")
            if (jClass != null && name != null) {
                feignClients.add(FeignClient(jClass, FeignClientArg(objectMapper.readValue(name))))
            }
        }

        return feignClients
    }
}
