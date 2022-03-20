package com.thoughtworks.archguard.packages.infrastructure

import com.thoughtworks.archguard.packages.domain.PackageRepository
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository


@Repository
class PackageRepositoryImpl : PackageRepository {

    @Autowired
    lateinit var jdbi: Jdbi

    override fun getPackageDependenceByModuleFull(systemId: Long, module: String): List<PackageDependenceDTO> {
        return jdbi.withHandle<List<PackageDependenceDTO>, Nothing> { handle ->
            handle.registerRowMapper(ConstructorMapper.factory(PackageDependenceDTO::class.java))
            handle
                    .createQuery("select a.clzname aClz, b.clzname bClz from code_method a, code_method b, code_ref_method_callees mc " +
                            "where a.id = mc.a and b.id = mc.b and a.module='$module' and b.module='$module' and a.system_id='$systemId' and b.system_id='$systemId' and mc.system_id='$systemId'")
                    .mapTo(PackageDependenceDTO::class.java)
                    .list()
        }
    }

    override fun getPackageDependenceByClass(systemId: Long, module: String): List<PackageDependenceDTO> {
        return jdbi.withHandle<List<PackageDependenceDTO>, Nothing> { handle ->
            handle.registerRowMapper(ConstructorMapper.factory(PackageDependenceDTO::class.java))
            handle
                .createQuery("select source as aClz, target as bClz from code_ref_class_dependencies where system_id='$systemId'")
                .mapTo(PackageDependenceDTO::class.java)
                .list()
        }
    }
}
