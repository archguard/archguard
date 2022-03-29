package org.archguard.scanner.bytecode

class ImportCollector {
    private val JAVA_LANG_PACKAGE = "java.lang"

    private var mapSimpleNames: MutableMap<String, String> = mutableMapOf()
    private val setNotImportedNames: Set<String> = HashSet()

    // set of field names in this class and all its predecessors.
    private val setFieldNames: Set<String> = HashSet()
    private val setInnerClassNames: Set<String> = HashSet()
    private val currentPackageSlash: String? = null
    private val currentPackagePoint: String? = null

    fun ImportCollector() {

    }

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

    private fun processNestedName(fullName: String): String {
        mapSimpleNames[fullName] = fullName
        return ""
    }

    fun packImports(): MutableMap<String, String> {
        return mapSimpleNames
    }

    fun processClassName(className: String) {
        if (className.startsWith(JAVA_LANG_PACKAGE)) {
            return
        }

        val names = splitPackageAndClassName(className)

        this.mapSimpleNames[className] = names.second
    }
}