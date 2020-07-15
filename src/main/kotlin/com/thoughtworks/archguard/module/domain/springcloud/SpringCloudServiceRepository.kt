package com.thoughtworks.archguard.module.domain.springcloud

import org.springframework.stereotype.Repository

@Repository
interface SpringCloudServiceRepository {
    fun getMethodIdsByClassId(classId: String): List<String>
    fun getServiceNameByMethodId(methodId: String): String
}
