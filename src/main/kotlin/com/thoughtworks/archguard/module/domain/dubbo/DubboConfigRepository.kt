package com.thoughtworks.archguard.module.domain.dubbo

interface DubboConfigRepository {
    fun getReferenceConfigBy(interfaceName: String, module: SubModuleDubbo): List<ReferenceConfig>
    fun getModuleByName(name: String): SubModuleDubbo?
    fun getServiceConfigBy(referenceConfig: ReferenceConfig): List<ServiceConfig>
}