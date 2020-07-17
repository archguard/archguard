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

    override fun getAll(): List<Dependency<JMethodVO>> {
        val sql = "select ${generateSelectMethodTemplate("a", "caller")}, ${generateSelectMethodTemplate("b", "callee")} " +
                "from JMethod a, JMethod b, `_MethodCallees` mc where a.id = mc.a and b.id = mc.b"

        return jdbi.withHandle<List<JMethodDependencyDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JMethodDependencyDto::class.java))
            it.createQuery(sql)
                    .mapTo(JMethodDependencyDto::class.java)
                    .list()
        }.map { it.toJMethodDependency() }
    }

    private fun generateSelectMethodTemplate(tableName: String, prefix: String): String{
        return "$tableName.module ${prefix}Module, $tableName.clzName ${prefix}Class, $tableName.name ${prefix}Method"
    }
}
