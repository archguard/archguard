package org.archguard.scanner.ctl.impl

import org.archguard.scanner.core.AnalyserSpec

enum class OfficialAnalyserSpecs(
    private val host: String,
    private val version: String,
    private val className: String,
) {
    // TODO, replace the host as the release url
    KOTLIN(
        host = "https://github.com/archguard/scanner/tree/master/analyser_sourcecode/lang_kotlin/src/test/resources/kotlin",
        version = "1.6.1",
        className = "KotlinAnalyser",
    ),
    TYPESCRIPT(
        host = "https://github.com/archguard/scanner/tree/master/analyser_sourcecode/lang_kotlin/src/test/resources/kotlin",
        version = "1.6.1",
        className = "TypeScriptAnalyser",
    ),
    JAVASCRIPT(
        host = "https://github.com/archguard/scanner/tree/master/analyser_sourcecode/lang_kotlin/src/test/resources/kotlin",
        version = "1.6.1",
        className = "TypeScriptAnalyser",
    ),
    JAVA(
        host = "https://github.com/archguard/scanner/tree/master/analyser_sourcecode/lang_kotlin/src/test/resources/kotlin",
        version = "1.6.1",
        className = "JavaAnalyser",
    ),
    API_CALLS(
        host = "https://github.com/archguard/scanner/tree/master/analyser_sourcecode/lang_kotlin/src/test/resources/kotlin",
        version = "1.6.1",
        className = "ApiCallAnalyser",
    ),
    ;

    fun spec(): AnalyserSpec {
        val identifier = name.lowercase()
        return AnalyserSpec(identifier, host, version, "$identifier-$version-all.jar", className)
    }

    companion object {
        fun specs() = values().map(OfficialAnalyserSpecs::spec)
    }
}
