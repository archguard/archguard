package org.archguard.model.code

import org.jdbi.v3.core.mapper.reflect.ColumnName

data class JAnnotation(
    @ColumnName("id") var id: String,
    @ColumnName("targetType") var targetType: String,
    @ColumnName("targetId") var targetId: String,
    @ColumnName("name") var name: String) {
    var values: Map<String, String>? = null
}
