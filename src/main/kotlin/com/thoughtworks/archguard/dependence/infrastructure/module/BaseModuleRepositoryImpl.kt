package com.thoughtworks.archguard.dependence.infrastructure.module

import com.thoughtworks.archguard.dependence.domain.module.BaseModuleRepository
import com.thoughtworks.archguard.dependence.domain.module.JClass
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

    override fun getJClassesById(id: String): JClass {
        val sql = "select id, name, module from JClass where id='$id'"
        return jdbi.withHandle<JClass, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClass::class.java))
            it.createQuery(sql)
                    .mapTo(JClass::class.java)
                    .one()
        }
    }


}
