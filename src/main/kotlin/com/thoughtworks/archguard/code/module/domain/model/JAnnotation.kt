package com.thoughtworks.archguard.code.module.domain.model

class JAnnotation(var id: String, var targetType: String, var targetId: String, var name: String) {
    var values: Map<String, String>? = null
}
