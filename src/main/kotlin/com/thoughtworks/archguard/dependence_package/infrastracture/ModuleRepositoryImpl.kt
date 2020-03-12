package com.thoughtworks.archguard.dependence_package.infrastracture

import com.thoughtworks.archguard.dependence_package.domain.dto.ModuleDefineDTO
import com.thoughtworks.archguard.dependence_package.domain.dto.ModuleDependenceDTO
import com.thoughtworks.archguard.dependence_package.domain.repository.ModuleRepository
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class ModuleRepositoryImpl : ModuleRepository {

    @Autowired
    lateinit var jdbi: Jdbi

    override fun getModuleDependence(packages: List<String>): List<ModuleDependenceDTO> {
        var aWhere = "a.clzname like '"
        var bWhere = "b.clzname like '"
        packages.forEachIndexed { index, s ->
            aWhere = if (index == 0) {
                "$aWhere$s%'"
            } else {
                "$aWhere or a.clzname like '$s%'"
            }

            bWhere = if (index == 0) {
                "$bWhere$s%'"
            } else {
                "$bWhere or b.clzname like '$s%'"
            }
        }

        return jdbi.withHandle<List<ModuleDependenceDTO>, Nothing> { handle ->
            handle.registerRowMapper(ConstructorMapper.factory(ModuleDependenceDTO::class.java))
            handle
                    .createQuery("select a.clzname aModule, b.clzname bModule from JMethod a, JMethod b, _MethodCallees mc where a.id = mc.a and b.id = mc.b and ($aWhere) and ($bWhere)")
                    .mapTo(ModuleDependenceDTO::class.java)
                    .list()
        }
    }

    override fun getModulePackages(): List<ModuleDefineDTO> {
        return jdbi.withHandle<List<ModuleDefineDTO>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(ModuleDefineDTO::class.java))
            it.createQuery("select name, packages from Modules")
                    .mapTo(ModuleDefineDTO::class.java)
                    .list()
        }
    }

}