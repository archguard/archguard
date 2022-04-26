package org.archguard.scanner.ctl.impl

import org.archguard.scanner.core.AnalyserSpec

private const val RELEASE_REPO = "https://github.com/archguard/scanner/releases/download/plugin-ea-0.0.4"

enum class OfficialAnalyserSpecs(
    private val url: String,
    private val version: String,
    private val className: String,
    private val isLanguage: Boolean = true,
) {
    CSHARP(
        RELEASE_REPO, "1.6.3", "CSharpAnalyser",
    ),
    GO(
        RELEASE_REPO, "1.6.3", "GoAnalyser",
    ),
    JAVA(
        RELEASE_REPO, "1.6.3", "JavaAnalyser",
    ),
    KOTLIN(
        RELEASE_REPO, "1.6.3", "KotlinAnalyser",
    ),
    PYTHON(
        RELEASE_REPO, "1.6.3", "PythonAnalyser",
    ),
    SCALA(
        RELEASE_REPO, "1.6.3", "ScalaAnalyser",
    ),
    TYPESCRIPT(
        RELEASE_REPO, "1.6.3", "TypeScriptAnalyser",
    ),
    JAVASCRIPT(
        TYPESCRIPT.url, TYPESCRIPT.version, TYPESCRIPT.className
    ),
    APICALLS(
        RELEASE_REPO, "1.6.3", "ApiCallAnalyser", false,
    ),
    DATAMAP(
        RELEASE_REPO, "1.6.3", "DataMapAnalyser", false,
    ),
    ;

    fun spec(): AnalyserSpec {
        val identifier = name.lowercase()
        val host = "$url/$version"
        val prefix = if (isLanguage) "lang" else "feat"
        val jar = "${prefix}_$identifier-$version-all.jar"

        return AnalyserSpec(identifier, host, version, jar, className)
    }

    companion object {
        fun specs() = values().map(OfficialAnalyserSpecs::spec)
    }
}
