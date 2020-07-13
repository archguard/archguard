package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.module.domain.model.JAnnotation

interface JAnnotationRepository {
    fun getJAnnotationByName(name: String): List<JAnnotation>
    fun getJAnnotationValues(annotation: String): Map<String, String>
    fun getJAnnotationWithValueByName(name: String): List<JAnnotation>
}
