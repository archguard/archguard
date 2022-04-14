package com.thoughtworks.archguard.code.clazz.infrastructure

import com.thoughtworks.archguard.code.clazz.domain.ClassRelation
import com.thoughtworks.archguard.code.clazz.domain.JClass
import com.thoughtworks.archguard.code.clazz.domain.JClassRepository
import com.thoughtworks.archguard.code.clazz.domain.JField
import com.thoughtworks.archguard.common.IdUtils.NOT_EXIST_ID
import com.thoughtworks.archguard.common.TypeMap
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class JClassRepositoryImpl(val jdbi: Jdbi) : JClassRepository {
    private val log = LoggerFactory.getLogger(JClassRepositoryImpl::class.java)

    override fun getJClassBy(systemId: Long, name: String, module: String?): JClass? {
        val sql = "select id, name, module, loc, access from code_class where system_id=:systemId and name=:name and module <=> :module"
        val jClassDto: JClassDto? = jdbi.withHandle<JClassDto, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassDto::class.java))
            val jClassDtos = it.createQuery(sql)
                .bind("systemId", systemId)
                .bind("name", name)
                .bind("module", module)
                .mapTo(JClassDto::class.java)

            jClassDtos.findFirst().orElse(null)
        }
        return jClassDto?.toJClass()
    }

    override fun findClassParents(systemId: Long, module: String, name: String): List<JClass> {
        val sql = "SELECT DISTINCT p.id as id, p.name as name, p.module as module, p.loc as loc, p.access as access " +
            "           FROM code_class c,code_ref_class_parent cp, code_class p" +
            "           WHERE c.id = cp.a AND cp.b = p.id" +
            "           And c.system_id = $systemId" +
            "           AND c.name = '$name' AND c.module = '$module'"
        return jdbi.withHandle<List<JClassDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassDto::class.java))
            it.createQuery(sql)
                .mapTo(JClassDto::class.java)
                .list()
        }.map { it.toJClass() }
    }

    override fun findClassImplements(systemId: Long, name: String, module: String): List<JClass> {
        val sql = "SELECT DISTINCT c.id as id, c.name as name, c.module as module, c.loc as loc, c.access as access " +
            "           FROM `code_class` c,`code_ref_class_parent` cp, `code_class` p" +
            "           WHERE c.id = cp.a AND cp.b = p.id" +
            "           AND c.system_id = $systemId" +
            "           AND p.name = '$name' AND p.module='$module'"
        return jdbi.withHandle<List<JClassDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassDto::class.java))
            it.createQuery(sql)
                .mapTo(JClassDto::class.java)
                .list()
        }.map { it.toJClass() }
    }

    override fun findCallees(systemId: Long, name: String, module: String): List<ClassRelation> {
        val sql = "SELECT b.clzname as clzname, b.module as module, COUNT(1) as count" +
            "                     FROM code_method a,`code_ref_method_callees` cl,code_method b" +
            "                     WHERE a.id = cl.a and b.id = cl.b" +
            "                     AND a.clzname = '$name' AND a.module = '$module'" +
            "                     AND b.clzname <> '$name'" +
            "                     AND a.system_id = b.system_id" +
            "                     AND a.system_id = $systemId" +
            "                     GROUP BY b.clzname, b.module"
        return jdbi.withHandle<List<ClassRelationDTO>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(ClassRelationDTO::class.java))
            it.createQuery(sql)
                .mapTo(ClassRelationDTO::class.java)
                .list()
        }.map { toClassRelation(systemId, it) }
    }

    override fun findCallers(systemId: Long, name: String, module: String): List<ClassRelation> {
        return jdbi.withHandle<List<ClassRelationDTO>, Nothing> {
            val sql = "SELECT a.clzname as clzname, a.module as module, COUNT(1) as count " +
                "                     FROM code_method a,`code_ref_method_callees` cl,code_method b" +
                "                     WHERE a.id = cl.a and b.id = cl.b" +
                "                     AND a.system_id = b.system_id" +
                "                     AND a.system_id = $systemId" +
                "                     AND a.clzname = '$name' AND a.module = '$module'" +
                "                     AND b.clzname <> '$name'" +
                "                     GROUP BY a.clzname, a.module"
            it.registerRowMapper(ConstructorMapper.factory(ClassRelationDTO::class.java))
            it.createQuery(sql)
                .mapTo(ClassRelationDTO::class.java)
                .list()
        }.map { toClassRelation(systemId, it) }
    }

    override fun findFields(id: String): List<JField> {
        val sql = "SELECT id, name, type FROM code_field WHERE id in (select b from code_ref_class_fields where a='$id')"
        return jdbi.withHandle<List<JField>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JField::class.java))
            it.createQuery(sql)
                .mapTo(JField::class.java)
                .list()
        }
    }

    private fun toClassRelation(systemId: Long, it: ClassRelationDTO): ClassRelation {
        val jClassByName = getJClassBy(systemId, it.clzname, it.module)
        return if (jClassByName == null) {
            ClassRelation(JClass(NOT_EXIST_ID, it.clzname, it.module), it.count)
        } else {
            ClassRelation(jClassByName, it.count)
        }
    }

    override fun findDependencers(id: String): List<JClass> {
        val sql = "select id, name, module, loc, access from code_class where id in (select a from code_ref_class_dependencies where b='$id' and b <> a)"
        return jdbi.withHandle<List<JClassDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassDto::class.java))
            it.createQuery(sql)
                .mapTo(JClassDto::class.java)
                .list()
        }.map { it.toJClass() }
    }

    override fun findDependencees(id: String): List<JClass> {
        val sql = "select id, name, module, loc, access from code_class where id in (select b from code_ref_class_dependencies where a='$id' and b <> a)"
        return jdbi.withHandle<List<JClassDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassDto::class.java))
            it.createQuery(sql)
                .mapTo(JClassDto::class.java)
                .list()
        }.map { it.toJClass() }
    }

    override fun getJClassById(id: String): JClass? {
        val sql = "select id, name, module, loc, access from code_class where id='$id'"
        return jdbi.withHandle<JClassDto, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassDto::class.java))
            it.createQuery(sql)
                .mapTo(JClassDto::class.java)
                .one()
        }.toJClass()
    }

    override fun getAllBySystemId(systemId: Long): List<JClass> {
        // todo: origin conditions is with ` and loc is not null`, when loc failure will not get results, so change it without loc.
        val sql = "SELECT id, name, module, loc, access FROM code_class where system_id = :systemId and is_test = false " +
            "and is_thirdparty = false"

        return jdbi.withHandle<List<JClassDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JClassDto::class.java))
            it.createQuery(sql)
                .bind("systemId", systemId)
                .mapTo(JClassDto::class.java)
                .list()
        }.map { it.toJClass() }
    }

    override fun getJClassesHasModules(systemId: Long): List<JClass> {
        return this.getAllBySystemId(systemId).filter { it.module != null }
    }
}

data class ClassRelationDTO(val clzname: String, val module: String?, val count: Int)

class JClassDto(val id: String, val name: String, val module: String?, val loc: Int?, val access: String?) {
    fun toJClass(): JClass {
        val jClass = JClass(id, name, module)
        if (access == null) {
            return jClass
        }
        val accessInt = access.toIntOrNull()
        if (accessInt != null) {
            TypeMap.getClassType(accessInt).forEach { jClass.addClassType(it) }
        }
        return jClass
    }
}
