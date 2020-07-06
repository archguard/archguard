package com.thoughtworks.archguard.module.infrastructure

object Utils {
    fun convertToNullIfStringValueEqualsNull(value: String): String? {
        return if (value == "null") null else value
    }
}