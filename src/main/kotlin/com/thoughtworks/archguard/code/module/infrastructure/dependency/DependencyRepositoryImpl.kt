package com.thoughtworks.archguard.code.module.infrastructure.dependency

import com.thoughtworks.archguard.code.module.domain.dependency.DependencyRepository
import com.thoughtworks.archguard.code.module.domain.model.Dependency
import com.thoughtworks.archguard.code.module.domain.model.JMethodVO
import com.thoughtworks.archguard.code.module.infrastructure.dto.JMethodDependencyDto
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
                "from code_method a, code_method b, `code_ref_method_callees` mc where a.module IS NOT null and b.module IS NOT null " +
                "and a.id = mc.a and b.id = mc.b and a.system_id = :systemId and b.system_id = :systemId and mc.system_id = :systemId " +
                "and a.is_test = false and b.is_test = false"

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
