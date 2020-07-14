package com.thoughtworks.archguard.module.domain.springcloud.feignclient

import com.thoughtworks.archguard.module.domain.JAnnotationRepository
import org.springframework.stereotype.Service
import java.lang.annotation.ElementType

@Service
class FeignClientService(val jAnnotationRepository: JAnnotationRepository) {

    fun getFeignClients(): List<FeignClient> {
        val feignClientAnnotations = jAnnotationRepository.getJAnnotationWithValueByName("feign.FeignClient").filter { it.targetType == ElementType.TYPE.name }
        return feignClientAnnotations.map { FeignClient(it.targetId, FeignClientArg(it.values.orEmpty())) }
    }


}
