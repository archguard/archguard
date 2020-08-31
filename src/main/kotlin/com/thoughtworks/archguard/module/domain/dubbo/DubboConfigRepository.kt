package com.thoughtworks.archguard.module.domain.dubbo

interface DubboConfigRepository {
    fun getReferenceConfigBy(systemId: Long, interfaceName: String, subModule: SubModuleDubbo): List<ReferenceConfig>
    fun getSubModuleByName(systemId: Long, name: String): SubModuleDubbo?
    fun getServiceConfigBy(systemId: Long, referenceConfig: ReferenceConfig): List<ServiceConfig>
}
