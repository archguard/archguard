package com.thoughtworks.archguard.method.infrastructure

import com.thoughtworks.archguard.clazz.infrastructure.JClassRepositoryImpl
import com.thoughtworks.archguard.method.domain.JMethod
import com.thoughtworks.archguard.method.domain.JMethodRepository
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class JMethodRepositoryImpl : JMethodRepository {
    private val log = LoggerFactory.getLogger(JClassRepositoryImpl::class.java)

    @Autowired
    lateinit var jdbi: Jdbi
    override fun findMethodsByModuleAndClass(module: String, name: String): List<JMethod> {
        val sql = "select id, name, clzname, module from JMethod where clzname='$name' and module='$module'"
        return jdbi.withHandle<List<JMethod>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JMethod::class.java))
            it.createQuery(sql)
                    .mapTo(JMethod::class.java)
                    .list()
        }
    }
}