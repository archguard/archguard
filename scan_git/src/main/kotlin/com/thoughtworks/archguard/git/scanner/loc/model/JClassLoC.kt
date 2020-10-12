package com.thoughtworks.archguard.git.scanner.loc.model

class JClassLoC(val module: String?) {
    var pkg: String? = null
    var clz: String? = null
    var loc = 0
    override fun toString(): String {
        return "JClassLoC{" +
                "pkg='" + pkg + '\'' +
                ", clz='" + clz + '\'' +
                ", module='" + module + '\'' +
                ", loc=" + loc +
                '}'
    }
}