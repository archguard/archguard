package com.thoughtworks.archguard.clazz.domain.service

import com.thoughtworks.archguard.clazz.domain.ClassRelation
import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.config.domain.ConfigureService
import org.springframework.stereotype.Service

@Service
class ClassConfigService(val configureService: ConfigureService) {

    fun buildClassRelationColorConfig(classRelations: List<ClassRelation>, projectId: Long) {
        classRelations.forEach { classRelation -> classRelation.clazz.buildColorConfigure(configureService.getNodeRelatedColorConfigures(projectId, classRelation.clazz.name)) }
    }

    fun buildJClassColorConfig(jClasses: List<JClass>, projectId: Long) {
        jClasses.forEach { jClass -> jClass.buildColorConfigure(configureService.getNodeRelatedColorConfigures(projectId, jClass.name)) }
    }
}