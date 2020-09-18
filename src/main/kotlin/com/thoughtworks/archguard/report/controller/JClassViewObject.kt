package com.thoughtworks.archguard.report.controller


data class JClassViewObject(val name: String, val packageViewObject: PackageViewObject) {
    companion object {
        fun create(fullName: String): JClassViewObject {
            val packageFullName = fullName.substring(0, fullName.indexOfLast { it == '.' })
            val className = fullName.substring(fullName.indexOfLast { it == '.' } + 1)
            return JClassViewObject(className, PackageViewObject.create(packageFullName))
        }
    }

}