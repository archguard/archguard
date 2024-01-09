package org.archguard.model.code

class JAnnotation(var id: String, var targetType: String, var targetId: String, var name: String) {
    var values: Map<String, String>? = null
}
