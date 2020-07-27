package com.thoughtworks.archguard.method.domain

class JMethod(val id: String, val name: String, val clazz: String, val module: String, val returnType: String?, val argumentTypes: List<String>) {
    var callees: List<JMethod> = ArrayList()
    var callers: List<JMethod> = ArrayList()
    var parents: List<JMethod> = ArrayList()
    var implements: List<JMethod> = ArrayList()
}
