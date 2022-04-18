package org.archguard.rule.core

interface Scanner<IN, OUT> {
    fun available(input: IN): Boolean
    fun execute(input: IN): OUT
}

fun main() {
    val scanner = object : Scanner<String, String> {
        override fun available(input: String): Boolean {
            return input.isNotEmpty()
        }

        override fun execute(input: String): String {
            return input.uppercase()
        }
    }
    println(scanner.execute("hello"))
}
