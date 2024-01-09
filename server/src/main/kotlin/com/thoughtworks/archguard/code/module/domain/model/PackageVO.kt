package com.thoughtworks.archguard.code.module.domain.model

import org.archguard.model.vos.JClassVO

class PackageVO(val packageName: String, val module: String) {

    fun containClass(jClassVO: JClassVO): Boolean {
        return module == jClassVO.module && jClassVO.name.startsWith("$packageName.")
    }

    fun directContainClass(jClassVO: JClassVO): Boolean {
        return module == jClassVO.module && jClassVO.getPackageName() == packageName
    }

    companion object {
        fun fromFullName(fullName: String): PackageVO {
            if (fullName == "..") {
                return PackageVO("", ".")
            }

            val startIndex = fullName.indexOfFirst { it == '.' }
            if (startIndex == -1) {
                return PackageVO("", fullName)
            }

            val moduleName = fullName.substring(0, startIndex)
            val name = fullName.substring(startIndex + 1)
            return PackageVO(name, moduleName)
        }
    }
}
