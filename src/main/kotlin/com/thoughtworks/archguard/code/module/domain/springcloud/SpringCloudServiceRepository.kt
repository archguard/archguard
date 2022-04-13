package com.thoughtworks.archguard.code.module.domain.springcloud

import com.thoughtworks.archguard.code.module.domain.model.JMethodVO
import org.springframework.stereotype.Repository

@Repository
interface SpringCloudServiceRepository {
    fun getMethodIdsByClassId(classId: String): List<String>
    fun getServiceNameByMethodId(methodId: String): String
    fun getMethodById(methodId: String): JMethodVO
}
