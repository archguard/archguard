package com.thoughtworks.archguard.module.infrastructure

import com.thoughtworks.archguard.module.domain.DubboConfigRepository
import com.thoughtworks.archguard.module.domain.ReferenceConfig
import com.thoughtworks.archguard.module.domain.ServiceConfig
import com.thoughtworks.archguard.module.domain.SubModule
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class DubboConfigRepositoryImpl : DubboConfigRepository {
    @Autowired
    lateinit var jdbi: Jdbi

    // FIXME: 可能会存在重复name的submodule
    override fun getModuleByName(name: String): SubModule {
        val sql = "select id, name, path from dubbo_module where name='$name'"
        return jdbi.withHandle<SubModule, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(SubModule::class.java))
            it.createQuery(sql)
                    .mapTo(SubModule::class.java)
                    .one()
        }
    }

    override fun getReferenceConfigBy(interfaceName: String, subModule: SubModule): List<ReferenceConfig> {
        val sql = "select id, referenceId, interface as interfaceName, version, group, module_id as moduleId " +
                "from dubbo_reference_config where interface='$interfaceName' and module_id='${subModule.id}'"
        val referenceConfigDtos = jdbi.withHandle<List<ReferenceConfigDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(ReferenceConfigDto::class.java))
            it.createQuery(sql)
                    .mapTo(ReferenceConfigDto::class.java)
                    .list()
        }
        return referenceConfigDtos.map { it.toReferenceConfigWithSubModule(subModule) }
    }

    override fun getServiceConfigBy(referenceConfig: ReferenceConfig): List<ServiceConfig> {
        val sql = "select sc.id, sc.interface as interfaceName, sc.ref, sc.version, sc.group, " +
                "sc.module_id as moduleId, m.name, m.path " +
                "from dubbo_service_config as sc and dubbo_module as m where sc.version='${referenceConfig.version} and " +
                "sc.group='${referenceConfig.group} and " +
                "sc.interface='${referenceConfig.interfaceName} and sc.module_id=m.id"
        val serviceConfigDto = jdbi.withHandle<List<ServiceConfigDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(ServiceConfigDto::class.java))
            it.createQuery(sql)
                    .mapTo(ServiceConfigDto::class.java)
                    .list()
        }
        return serviceConfigDto.map { it.toServiceConfig() }
    }
}

class ReferenceConfigDto(val id: String, private val referenceId: String, private val interfaceName: String,
                         private val version: String, private val group: String, private val moduleId: String) {
    fun toReferenceConfigWithSubModule(subModule: SubModule): ReferenceConfig {
        if (subModule.id == moduleId) {
            return ReferenceConfig(id, referenceId, interfaceName, version, group, subModule)
        }
        throw RuntimeException("SubModule Not Matching!")
    }
}

class ServiceConfigDto(val id: String, val interfaceName: String, val ref: String, val version: String,
                       val group: String, val moduleId: String, val name: String, val path: String) {
    fun toServiceConfig(): ServiceConfig {
        return ServiceConfig(id, interfaceName, ref, version, group, SubModule(moduleId, name, path))
    }
}


