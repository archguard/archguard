package com.thoughtworks.archguard.report.domain.circulardependency

data class ClassVO(val moduleName: String, val packageName: String, val className: String) {
    companion object {
        fun create(fullName: String): ClassVO {
            val moduleName = fullName.substring(0, fullName.indexOfFirst { it == '.' })
            val packageName = fullName.substring(fullName.indexOfFirst { it == '.' } + 1, fullName.indexOfLast { it == '.' })
            val className = fullName.substring(fullName.indexOfLast { it == '.' } + 1)
            return ClassVO(moduleName, packageName, className)
        }
    }
}