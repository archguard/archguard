package com.thoughtworks.archguard.code.module.infrastructure

import com.thoughtworks.archguard.code.module.domain.JAnnotationRepository
import org.archguard.model.code.JAnnotation
import com.thoughtworks.archguard.code.module.infrastructure.dto.JAnnotationValueDto
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class JAnnotationRepositoryImpl : JAnnotationRepository {

    @Autowired
    lateinit var jdbi: Jdbi

    override fun getJAnnotationByName(name: String): List<JAnnotation> {
        val sql = "select * from code_annotation where name like '%$name%'"
        return jdbi.withHandle<List<JAnnotation>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JAnnotation::class.java))
            it.createQuery(sql)
                .mapTo(JAnnotation::class.java)
                .list()
        }
    }

    override fun getJAnnotationValues(annotationId: String): Map<String, String> {
        val sql = "select * from code_annotation_value where annotationId = '$annotationId'"
        val list = jdbi.withHandle<List<JAnnotationValueDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(JAnnotationValueDto::class.java))
            it.createQuery(sql)
                .mapTo(JAnnotationValueDto::class.java)
                .list()
        }

        return list.associateBy({ it.key }, { it.value })
    }

    override fun getJAnnotationWithValueByName(name: String): List<JAnnotation> {
        val jAnnotations = getJAnnotationByName(name)
        jAnnotations.forEach { it.values = getJAnnotationValues(it.id) }
        return jAnnotations
    }
}
