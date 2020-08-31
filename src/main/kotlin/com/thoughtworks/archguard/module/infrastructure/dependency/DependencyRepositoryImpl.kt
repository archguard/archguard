package com.thoughtworks.archguard.module.infrastructure.dependency

import com.thoughtworks.archguard.module.domain.dependency.DependencyRepository
import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JMethodVO
import com.thoughtworks.archguard.module.infrastructure.dto.JMethodDependencyDto
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class DependencyRepositoryImpl : DependencyRepository {

    @Autowired
    lateinit var jdbi: Jdbi

    override fun getAllMethodDependencies(systemId: Long): List<Dependency<JMethodVO>> {
        val sql = "select ${generateSelectMethodTemplate("a", "caller")}, ${generateSelectMethodTemplate("b", "callee")} " +
                "from JMethod a, JMethod b, `_MethodCallees` mc where a.module != 'null' and b.module != 'null' and a.id = mc.a and b.id = mc.b and a.system_id = :systemId and b.system_id = :systemId and mc.system_id = :systemId"

        return jdbi.withHandle<List<JMethodDependencyDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JMethodDependencyDto::class.java))
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .mapTo(JMethodDependencyDto::class.java)
                    .list()
        }.map { it.toJMethodDependency() }
    }

    private fun generateSelectMethodTemplate(tableName: String, prefix: String): String {
        return "$tableName.module ${prefix}Module, $tableName.clzName ${prefix}Class, $tableName.name ${prefix}Method, $tableName.returntype ${prefix}ReturnType, $tableName.argumenttypes ${prefix}ArgumentTypes"
    }

}
