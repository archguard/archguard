package org.archguard.scanner.bytecode

class ImportCollector {
    private val JAVA_LANG_PACKAGE = "java."
    private val KOTLIN_LANG_PACKAGE = "kotlin."

    private val DEFAULT_TYPE_NAMES = arrayOf(
        "byte",
        "char",
        "double",
        "float",
        "int",
        "long",
        "short",
        "boolean"
    )


    private var mapSimpleNames: MutableMap<String, String> = mutableMapOf()

    fun splitPackageAndClassName(fullName: String): Pair<String, String> {
        val lastDot = fullName.lastIndexOf('.')
        var packageName = fullName
        var className = fullName
        if (lastDot > 0) {
            packageName = fullName.substring(0, lastDot)
            className = fullName.substring(lastDot + 1, fullName.length)
        }

        return Pair(packageName, className)
    }

    fun packImports(): MutableMap<String, String> {
        return mapSimpleNames
    }

    fun addImport(className: String) {
        if (className.startsWith(JAVA_LANG_PACKAGE) || className.startsWith(KOTLIN_LANG_PACKAGE)) {
            return
        }

        if (DEFAULT_TYPE_NAMES.contains(className)) {
            return;
        }

        val names = splitPackageAndClassName(className)
        this.mapSimpleNames[className] = names.second
    }
}