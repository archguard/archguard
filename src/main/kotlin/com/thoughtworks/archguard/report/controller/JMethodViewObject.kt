package com.thoughtworks.archguard.report.controller

data class JMethodViewObject(val name: String, val argsString: String, val jClassViewObject: JClassViewObject) {
    companion object {
        fun create(fullName: String): JMethodViewObject {
            val methodFullName = fullName.substring(0, fullName.indexOfFirst { it == '(' })
            val name = methodFullName.substring(methodFullName.indexOfLast { it == '.' })
            val classFullName = methodFullName.substring(0, methodFullName.indexOfLast { it == '.' })
            val argsString = fullName.substring(fullName.indexOfFirst { it == '(' + 1 }, fullName.indexOfLast { it == ')' })
            return JMethodViewObject(name, argsString, JClassViewObject.create(classFullName))
        }
    }
}
