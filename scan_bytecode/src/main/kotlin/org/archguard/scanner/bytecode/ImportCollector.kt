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


    public fun ImportCollector() {

    }

    private fun processNestedName(fullName: String): String {
        mapSimpleNames[fullName] = fullName
        return ""
    }

    private fun packImports(): List<String> {
        return arrayListOf()
    }
}