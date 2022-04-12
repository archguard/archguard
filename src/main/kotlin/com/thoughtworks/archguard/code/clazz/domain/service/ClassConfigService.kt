package com.thoughtworks.archguard.code.clazz.domain.service

import com.thoughtworks.archguard.code.clazz.domain.ClassRelation
import com.thoughtworks.archguard.code.clazz.domain.JClass
import com.thoughtworks.archguard.config.domain.ConfigureService
import org.springframework.stereotype.Service

@Service
class ClassConfigService(val configureService: ConfigureService) {

    fun buildClassRelationColorConfig(classRelations: List<ClassRelation>, systemId: Long) {
        classRelations.forEach { classRelation -> classRelation.clazz.buildColorConfigure(configureService.getNodeRelatedColorConfigures(systemId, classRelation.clazz.name)) }
    }

    fun buildJClassColorConfig(jClasses: List<JClass>, systemId: Long) {
        jClasses.forEach { jClass -> jClass.buildColorConfigure(configureService.getNodeRelatedColorConfigures(systemId, jClass.name)) }
    }
}