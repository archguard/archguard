package com.thoughtworks.archguard.module.domain.dubbo

interface DubboConfigRepository {
    fun getReferenceConfigBy(projectId: Long, interfaceName: String, subModule: SubModuleDubbo): List<ReferenceConfig>
    fun getSubModuleByName(projectId: Long, name: String): SubModuleDubbo?
    fun getServiceConfigBy(projectId: Long, referenceConfig: ReferenceConfig): List<ServiceConfig>
}
