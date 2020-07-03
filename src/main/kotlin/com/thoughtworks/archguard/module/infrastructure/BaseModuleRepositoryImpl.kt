package com.thoughtworks.archguard.module.infrastructure

import com.thoughtworks.archguard.module.domain.BaseModuleRepository
import com.thoughtworks.archguard.module.domain.model.JClass
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class BaseModuleRepositoryImpl : BaseModuleRepository {

    @Autowired
    lateinit var jdbi: Jdbi

    override fun getBaseModules(): List<String> {
        return jdbi.withHandle<List<String>, Nothing> {
            it.createQuery("select distinct module from JClass")
                    .mapTo(String::class.java)
                    .list()
                    .filter { it -> it != "null" }
        }
    }

    override fun getJClassesHasModules(): List<JClass> {
        val sql = "select id, name, module from JClass"
        return jdbi.withHandle<List<JClass>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClass::class.java))
            it.createQuery(sql)
                    .mapTo(JClass::class.java)
                    .list()
                    .filter { it -> it.module != "null" }
        }
    }
}
