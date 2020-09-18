package com.thoughtworks.archguard.report.controller

data class PackageViewObject(val moduleViewObject: ModuleViewObject, val name: String) {
    companion object {
        fun create(fullName: String): PackageViewObject {
            return PackageViewObject(ModuleViewObject(fullName.substring(0, fullName.indexOfFirst { it == '.' })),
                    fullName.substring(fullName.indexOfFirst { it == '.' } + 1))
        }
    }
}