package com.thoughtworks.archguard.git.scanner.loc.model

class JMethodLoC(val methodName: String, val loc: Int) {
    override fun toString(): String {
        return "JMethodLoC{" +
                ", methodName='" + methodName + '\'' +
                ", loc=" + loc +
                '}'
    }
}