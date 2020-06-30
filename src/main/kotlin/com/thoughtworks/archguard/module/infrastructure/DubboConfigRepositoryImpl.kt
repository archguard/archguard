package com.thoughtworks.archguard.module.infrastructure

import com.thoughtworks.archguard.module.domain.DubboConfigRepository
import com.thoughtworks.archguard.module.domain.ReferenceConfig
import com.thoughtworks.archguard.module.domain.ServiceConfig
import com.thoughtworks.archguard.module.domain.SubModule
import org.springframework.stereotype.Repository

@Repository
class DubboConfigRepositoryImpl : DubboConfigRepository {

    override fun getModuleBy(name: String): SubModule {
        TODO("Not yet implemented")
    }

    override fun getReferenceConfigBy(interfaceName: String, module: SubModule): List<ReferenceConfig> {
        TODO("Not yet implemented")
    }

    override fun getServiceConfigBy(referenceConfig: ReferenceConfig): List<ServiceConfig> {
        TODO("Not yet implemented")
    }
}