package org.archguard.operator

import org.archguard.model.code.JClass

fun checkIsDataClass(jClass: JClass): Boolean {
    if (jClass.fields.size * 2 != jClass.methods.size) {
        return false
    }

    jClass.fields.forEach {
        val methodNames = jClass.methods.map { it.name }.toMutableList()
        if (it.type in listOf("java.lang.Boolean", "boolean")) {
            if (!methodNames.contains("is" + capitalize(it.name)) || !methodNames.contains("set" + capitalize(it.name))) {
                return false
            }
        } else if (!methodNames.contains("get" + capitalize(it.name)) || !methodNames.contains("set" + capitalize(it.name))) {
            return false
        }
    }
    return true
}

fun capitalize(line: String): String {
    if (line.isEmpty()) {
        return line
    }
    return Character.toUpperCase(line[0]).toString() + line.substring(1)
}