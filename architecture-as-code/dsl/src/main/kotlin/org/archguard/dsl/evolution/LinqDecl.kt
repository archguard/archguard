package org.archguard.dsl.evolution

import org.archguard.dsl.base.Element
import org.archguard.dsl.evolution.linq.ClassQueryDecl
import org.archguard.dsl.evolution.linq.ImplementationDecl

/**
 * for all query
 */
open class QueryConditionDecl: Element {}

fun implementation(expr: String): ImplementationDecl {
    return ImplementationDecl(expr)
}

fun clazz(condition: QueryConditionDecl): ClassQueryDecl {
    return struct(condition)
}

fun struct(condition: QueryConditionDecl): ClassQueryDecl {
    val classQueryDecl = ClassQueryDecl(condition)
//    classQueryDecl.init()
    return classQueryDecl
}