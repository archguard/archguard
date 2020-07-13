package com.thoughtworks.archguard.module.domain.dubbo

interface DubboConfigRepository {
    fun getReferenceConfigBy(interfaceName: String, subModule: SubModuleDubbo): List<ReferenceConfig>
    fun getSubModuleByName(name: String): SubModuleDubbo?
    fun getServiceConfigBy(referenceConfig: ReferenceConfig): List<ServiceConfig>
}