package com.thoughtworks.archguard.evaluation.infrastructure

inline fun <reified T : Enum<T>> enumContains(name: String): Boolean {
    return enumValues<T>().any { it.name == name }
}