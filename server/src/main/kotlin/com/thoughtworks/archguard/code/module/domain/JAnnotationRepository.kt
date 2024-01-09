package com.thoughtworks.archguard.code.module.domain

import org.archguard.model.code.JAnnotation

interface JAnnotationRepository {
    fun getJAnnotationByName(name: String): List<JAnnotation>
    fun getJAnnotationValues(annotation: String): Map<String, String>
    fun getJAnnotationWithValueByName(name: String): List<JAnnotation>
}
