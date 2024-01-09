package com.thoughtworks.archguard.code.module.domain.dubbo

import org.archguard.protocol.dubbo.ReferenceConfig
import org.archguard.protocol.dubbo.ServiceConfig
import org.archguard.protocol.dubbo.SubModuleDubbo

interface DubboConfigRepository {
    fun getReferenceConfigBy(systemId: Long, interfaceName: String, subModule: SubModuleDubbo): List<ReferenceConfig>
    fun getSubModuleByName(systemId: Long, name: String): SubModuleDubbo?
    fun getServiceConfigBy(systemId: Long, referenceConfig: ReferenceConfig): List<ServiceConfig>
}
