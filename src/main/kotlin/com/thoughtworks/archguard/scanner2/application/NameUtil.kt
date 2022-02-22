package com.thoughtworks.archguard.scanner2.application

fun getModuleNameFromPackageFullName(fullName: String): String {
    return fullName.substring(0, fullName.indexOfFirst { it == '.' } + 1)
}

fun getPackageNameFromPackageFullName(fullName: String): String {
    return fullName.substring(fullName.indexOfFirst { it == '.' } + 1)
}