package com.thoughtworks.archguard.clazz.infrastructure

import com.thoughtworks.archguard.clazz.domain.ClassRelation
import com.thoughtworks.archguard.clazz.domain.FullName
import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.clazz.infrastructure.dto.JClassDto
import com.thoughtworks.archguard.common.IdUtils.NOT_EXIST_ID
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class JClassRepositoryImpl : JClassRepository {
    private val log = LoggerFactory.getLogger(JClassRepositoryImpl::class.java)

    @Autowired
    lateinit var jdbi: Jdbi

    override fun getJClassBy(name: String, module: String): JClass? {
        val sql = "select id, name, module, loc, access from JClass where name='$name' and module='$module'"
        val jClassDto: JClassDto? = jdbi.withHandle<JClassDto, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassDto::class.java))
            it.createQuery(sql)
                    .mapTo(JClassDto::class.java)
                    .findOne().orElse(null)
        }
        return jClassDto?.toJClass()
    }

    override fun findClassParents(module: String?, name: String?): List<JClass> {
        var moduleFilter = ""
        if (!module.isNullOrEmpty()) {
            moduleFilter = "and c.module='$module'"
        }
        val sql = "SELECT DISTINCT p.id as id, p.name as name, p.module as module, p.loc as loc, p.access as access " +
                "           FROM JClass c,`_ClassParent` cp,JClass p" +
                "           WHERE c.id = cp.a AND cp.b = p.id" +
                "           AND c.name = '$name' $moduleFilter"
        return jdbi.withHandle<List<JClassDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassDto::class.java))
            it.createQuery(sql)
                    .mapTo(JClassDto::class.java)
                    .list()
        }.map { it.toJClass() }
    }

    override fun findClassImplements(name: String?, module: String?): List<JClass> {
        var moduleFilter = ""
        if (!module.isNullOrEmpty()) {
            moduleFilter = "and p.module='$module'"
        }
        val sql = "SELECT DISTINCT c.id as id, c.name as name, c.module as module, c.loc as loc, c.access as access " +
                "           FROM JClass c,`_ClassParent` cp,JClass p" +
                "           WHERE c.id = cp.a AND cp.b = p.id" +
                "           AND p.name = '$name' $moduleFilter"
        return jdbi.withHandle<List<JClassDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassDto::class.java))
            it.createQuery(sql)
                    .mapTo(JClassDto::class.java)
                    .list()
        }.map { it.toJClass() }
    }

    override fun findCallees(name: String?, module: String?): List<ClassRelation> {
        var moduleFilter = ""
        if (!module.isNullOrEmpty()) {
            moduleFilter = "and a.module='$module'"
        }
        val sql = "SELECT b.clzname as clzname, b.module as module, COUNT(1) as count" +
                "                     FROM JMethod a,`_MethodCallees` cl,JMethod b" +
                "                     WHERE a.id = cl.a and b.id = cl.b" +
                "                     AND a.clzname = '$name' $moduleFilter" +
                "                     AND b.clzname <> '$name'" +
                "                     GROUP BY b.clzname, b.module"
        return jdbi.withHandle<List<ClassRelationDTO>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(ClassRelationDTO::class.java))
            it.createQuery(sql)
                    .mapTo(ClassRelationDTO::class.java)
                    .list()
        }.map { toClassRelation(it) }
    }

    override fun findCallers(name: String?, module: String?): List<ClassRelation> {
        var moduleFilter = ""
        if (!module.isNullOrEmpty()) {
            moduleFilter = "and a.module='$module'"
        }
        val sql = "SELECT a.clzname as clzname, a.module as module, COUNT(1) as count " +
                "                     FROM JMethod a,`_MethodCallees` cl,JMethod b" +
                "                     WHERE a.id = cl.a and b.id = cl.b" +
                "                     AND b.clzname = '$name' $moduleFilter" +
                "                     AND a.clzname <> '$name'" +
                "                     GROUP BY a.clzname, a.module"
        return jdbi.withHandle<List<ClassRelationDTO>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(ClassRelationDTO::class.java))
            it.createQuery(sql)
                    .mapTo(ClassRelationDTO::class.java)
                    .list()
        }.map { toClassRelation(it) }
    }

    private fun toClassRelation(it: ClassRelationDTO): ClassRelation {
        val jClassByName = getJClassBy(it.clzname, it.module)
        return if (jClassByName == null) {
            ClassRelation(JClass(NOT_EXIST_ID, it.clzname, it.module), it.count)
        } else {
            ClassRelation(jClassByName, it.count)
        }
    }

    override fun findDependencers(id: String): List<JClass> {
        val sql = "select id, name, module, loc, access from JClass where id in (select a from _ClassDependences where b='${id}' and b <> a)"
        return jdbi.withHandle<List<JClassDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassDto::class.java))
            it.createQuery(sql)
                    .mapTo(JClassDto::class.java)
                    .list()
        }.map { it.toJClass() }
    }

    override fun findDependencees(id: String): List<JClass> {
        val sql = "select id, name, module, loc, access from JClass where id in (select b from _ClassDependences where a='${id}' and b <> a)"
        return jdbi.withHandle<List<JClassDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassDto::class.java))
            it.createQuery(sql)
                    .mapTo(JClassDto::class.java)
                    .list()
        }.map { it.toJClass() }
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
        val sql = "SELECT id, name, module, loc, access FROM JClass"
        return jdbi.withHandle<List<JClassDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassDto::class.java))
            it.createQuery(sql)
                    .mapTo(JClassDto::class.java)
                    .list()
        }.map { it.toJClass() }
    }

    override fun getAll(fullNames: List<FullName>): List<JClass> {
        val sql = "SELECT id, name, module, loc, access FROM JClass WHERE concat(module, '.', name) IN (<fullNameList>)"
        return jdbi.withHandle<List<JClassDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassDto::class.java))
            it.createQuery(sql)
                    .bindList("fullNameList", fullNames.map { fullName -> "${fullName.module}.${fullName.name}" })
                    .mapTo(JClassDto::class.java)
                    .list()
        }.map { it.toJClass() }
    }

    override fun getJClassesHasModules(): List<JClass> {
        return this.getAll().filter { it.module != "null" }
    }

}

data class ClassRelationDTO(val clzname: String, val module: String, val count: Int)
