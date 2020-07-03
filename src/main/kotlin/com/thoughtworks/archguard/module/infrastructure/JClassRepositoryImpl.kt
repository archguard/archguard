package com.thoughtworks.archguard.module.infrastructure

import com.thoughtworks.archguard.module.domain.JClassRepository
import com.thoughtworks.archguard.module.domain.model.JClass
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class JClassRepositoryImpl : JClassRepository {
    @Autowired
    lateinit var jdbi: Jdbi

    override fun getJClassBy(name: String, module: String): JClass? {
        val sql = "select id, name, module from JClass where name='$name' and module='$module'"
        return jdbi.withHandle<JClass, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClass::class.java))
            it.createQuery(sql)
                    .mapTo(JClass::class.java)
                    .one()
        }
    }

    override fun getJClassById(id: String): JClass? {
        val sql = "select id, name, module from JClass where id='$id'"
        return jdbi.withHandle<JClass, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClass::class.java))
            it.createQuery(sql)
                    .mapTo(JClass::class.java)
                    .one()
        }
    }
}