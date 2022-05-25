package org.archguard.dsl.evolution

import org.archguard.dsl.evolution.linq.ClassQueryDecl
import org.archguard.dsl.evolution.linq.ImplementationDecl
import org.archguard.dsl.evolution.linq.Linq

fun implementation(expr: String): ImplementationDecl {
    return ImplementationDecl(expr)
}

fun clazz(condition: Linq): ClassQueryDecl {
    return struct(condition)
}

fun struct(condition: Linq): ClassQueryDecl {
    val classQueryDecl = ClassQueryDecl(condition)
//    classQueryDecl.init()
    return classQueryDecl
}