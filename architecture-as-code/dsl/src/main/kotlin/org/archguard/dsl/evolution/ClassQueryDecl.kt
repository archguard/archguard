package org.archguard.dsl.evolution

import org.archguard.dsl.base.Element

/**
 * for all query
 */
open class QueryConditionDecl: Element {}

class ClassQueryDecl(condition: QueryConditionDecl) : Element {
    // todo: re
    fun filter(function: () -> List<Unit>) {

    }

    fun map(function: () -> Unit) {

    }
}

class ImplementationDecl(expr: String) : QueryConditionDecl() {

}

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