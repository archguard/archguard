package com.thoughtworks.archguard.code.module.domain

import com.thoughtworks.archguard.code.module.domain.model.JAnnotation

interface JAnnotationRepository {
    fun getJAnnotationByName(name: String): List<JAnnotation>
    fun getJAnnotationValues(annotation: String): Map<String, String>
    fun getJAnnotationWithValueByName(name: String): List<JAnnotation>
}
