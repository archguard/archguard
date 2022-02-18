package com.thoughtworks.archguard.evaluation_bak.infrastructure

inline fun <reified T : Enum<T>> enumContains(name: String): Boolean {
    return enumValues<T>().any { it.name == name }
}