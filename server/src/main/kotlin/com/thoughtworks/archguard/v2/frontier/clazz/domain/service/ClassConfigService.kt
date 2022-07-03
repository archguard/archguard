package com.thoughtworks.archguard.v2.frontier.clazz.domain.service

import com.thoughtworks.archguard.v2.frontier.clazz.domain.ClassRelation
import com.thoughtworks.archguard.v2.frontier.clazz.domain.JClass
import com.thoughtworks.archguard.config.domain.ConfigureService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ClassConfigService(
    @Autowired private val configureService: ConfigureService
) {
    fun buildClassRelationColorConfig(classRelations: List<ClassRelation>, systemId: Long) {
        buildJClassColorConfig(classRelations.map(ClassRelation::clazz), systemId)
    }

    fun buildJClassColorConfig(jClasses: List<JClass>, systemId: Long) {
        jClasses.forEach { jClass ->
            jClass.buildColorConfigure(
                configureService.getNodeRelatedColorConfigures(systemId, jClass.name)
            )
        }
    }
}
