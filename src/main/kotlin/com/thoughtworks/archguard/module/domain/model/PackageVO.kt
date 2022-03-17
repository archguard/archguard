package com.thoughtworks.archguard.module.domain.model

class PackageVO(val packageName: String, val module: String) {

    fun containClass(jClassVO: JClassVO): Boolean {
        return module == jClassVO.module && jClassVO.name.startsWith("$packageName.")
    }

    fun directContainClass(jClassVO: JClassVO): Boolean {
        return module == jClassVO.module && jClassVO.getPackageName() == packageName
    }

    companion object {
        fun fromFullName(fullName: String): PackageVO {
            val startIndex = fullName.indexOfFirst { it == '.' }
            if (startIndex == -1) {
                return PackageVO("", fullName)
            }
            var moduleName = fullName.substring(0, startIndex)
            val name = fullName.substring(startIndex + 1)
            if (moduleName == "root") {
                moduleName = "."
            }

            return PackageVO(name, moduleName)
        }
    }
}