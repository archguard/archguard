package com.thoughtworks.archguard.git.scanner.loc.model

class JMethodLoC(private val pkg: String, private val clz: String, private val methodName: String, private val loc: Int) {
    override fun toString(): String {
        return "JMethodLoC{" +
                "pkg='" + pkg + '\'' +
                ", clz='" + clz + '\'' +
                ", methodName='" + methodName + '\'' +
                ", loc=" + loc +
                '}'
    }
}