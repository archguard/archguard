package com.thoughtworks.archguard.report.domain.circulardependency

data class PackageVO(val moduleName: String, val packageName: String) {
    companion object {
        fun create(fullName: String): PackageVO {
            val moduleName = fullName.substring(0, fullName.indexOfFirst { it == '.' })
            val packageName = fullName.substring(fullName.indexOfFirst { it == '.' } + 1)
            return PackageVO(moduleName, packageName)
        }
    }
}