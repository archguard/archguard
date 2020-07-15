package com.thoughtworks.archguard.module.domain.springcloud.feignclient

interface FeignClientRepository {
    fun getServiceNameByMethodId(methodId: String): String
}
