package com.thoughtworks.archguard.report.domain.redundancy

data class DataClass(val moduleName: String, val packageName: String, val className: String, val fields: List<String>)
