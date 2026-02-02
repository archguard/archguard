package org.archguard.linter.rule.protobuf.rules

internal object ProtobufNaming {
    // Mirrors the spirit of protolint's naming rules, but kept intentionally small/customizable.
    // - UpperCamelCase: FooBar, FooBar2
    // - lower_snake_case: foo_bar, foo2_bar3
    private val upperCamelCase = Regex("^[A-Z][a-zA-Z0-9]*$")
    private val lowerSnakeCase = Regex("^[a-z0-9]+(_[a-z0-9]+)*$")

    fun isUpperCamelCase(name: String): Boolean = upperCamelCase.matches(name)
    fun isLowerSnakeCase(name: String): Boolean = lowerSnakeCase.matches(name)
}

