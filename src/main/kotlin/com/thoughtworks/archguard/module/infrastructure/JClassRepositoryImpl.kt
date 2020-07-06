package com.thoughtworks.archguard.module.infrastructure

import com.thoughtworks.archguard.module.domain.JClassRepository
import com.thoughtworks.archguard.module.domain.model.JClass
import com.thoughtworks.archguard.module.infrastructure.dto.JClassDto
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class JClassRepositoryImpl : JClassRepository {
    @Autowired
    lateinit var jdbi: Jdbi

    override fun getJClassBy(name: String, module: String): JClass? {
        val sql = "select id, name, module, loc, access from JClass where name='$name' and module='$module'"
        return jdbi.withHandle<JClassDto, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassDto::class.java))
            it.createQuery(sql)
                    .mapTo(JClassDto::class.java)
                    .one()
        }.toJClass()
    }

    override fun getJClassById(id: String): JClass? {
        val sql = "select id, name, module, loc, access from JClass where id='$id'"
        return jdbi.withHandle<JClassDto, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassDto::class.java))
            it.createQuery(sql)
                    .mapTo(JClassDto::class.java)
                    .one()
        }.toJClass()
    }

    override fun getAll(): List<JClass> {
        val sql = "select id, name, module from JClass"
        return jdbi.withHandle<List<JClassDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassDto::class.java))
            it.createQuery(sql)
                    .mapTo(JClassDto::class.java)
                    .list()
        }.map { it.toJClass() }
    }

    override fun getJClassesHasModules(): List<JClass> {
        return this.getAll().filter { it.module != "null" }
    }
}