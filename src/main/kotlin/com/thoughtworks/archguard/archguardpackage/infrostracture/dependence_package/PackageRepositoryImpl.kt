package com.thoughtworks.archguard.archguardpackage.infrostracture

import com.thoughtworks.archguard.archguardpackage.domain.dto.PackageDependenceDTO
import com.thoughtworks.archguard.archguardpackage.domain.repository.PackageRepository
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository


@Repository
class PackageRepositoryImpl : PackageRepository {

    @Autowired
    lateinit var jdbi: Jdbi

    override fun getPackageDependence(): List<PackageDependenceDTO> {
        return jdbi.withHandle<List<PackageDependenceDTO>, Nothing> { handle ->
            handle.registerRowMapper(ConstructorMapper.factory(PackageDependenceDTO::class.java))
            handle
                    .createQuery("select a.clzname aClz, b.clzname bClz from JMethod a, JMethod b, _MethodCallees mc where a.id = mc.a and b.id = mc.b")
                    .mapTo(PackageDependenceDTO::class.java)
                    .list()
        }
    }

}