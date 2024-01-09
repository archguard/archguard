package com.thoughtworks.archguard.report.domain.redundancy

import org.archguard.model.vos.FieldVO

data class DataClass(val moduleName: String, val packageName: String, val className: String, val fields: List<FieldVO>)
