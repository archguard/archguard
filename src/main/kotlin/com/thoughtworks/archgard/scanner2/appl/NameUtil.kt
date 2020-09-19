package com.thoughtworks.archgard.scanner2.appl

fun getModuleNameFromPackageFullName(fullName: String): String {
    return fullName.substring(0, fullName.indexOfFirst { it == '.' })
}

fun getPackageNameFromPackageFullName(fullName: String): String {
    return fullName.substring(fullName.indexOfFirst { it == '.' } + 1)
}