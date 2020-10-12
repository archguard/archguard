package com.thoughtworks.archguard.git.scanner.loc.model

class JClassLoC(val module: String?) {
    var pkg: String? = null
    var clz: String? = null
    var loc = 0
    var methodLocs: ArrayList<JMethodLoC> = ArrayList()
    override fun toString(): String {
        return "JClassLoC{" +
                "pkg='" + pkg + '\'' +
                ", clz='" + clz + '\'' +
                ", module='" + module + '\'' +
                ", loc=" + loc +
                '}'
    }

    fun addMethod(methodName: String, loc: Int) {
        methodLocs.add(JMethodLoC(methodName, loc))
    }
}