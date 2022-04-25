package org.archguard.scanner.ctl.impl

import org.archguard.scanner.core.AnalyserSpec

// TODO, replace the host as the release url
private const val RELEASE_REPO = "https://github.com/archguard/scanner/releases/download"

enum class OfficialAnalyserSpecs(
    private val url: String,
    private val version: String,
    private val className: String,
    private val isLanguage: Boolean = true,
) {
    CSHARP(
        RELEASE_REPO, "1.6.1", "CSharpAnalyser",
    ),
    GO(
        RELEASE_REPO, "1.6.1", "GoAnalyser",
    ),
    JAVA(
        RELEASE_REPO, "1.6.1", "JavaAnalyser",
    ),
    KOTLIN(
        RELEASE_REPO, "1.6.1", "KotlinAnalyser",
    ),
    PYTHON(
        RELEASE_REPO, "1.6.1", "PythonAnalyser",
    ),
    SCALA(
        RELEASE_REPO, "1.6.1", "ScalaAnalyser",
    ),
    TYPESCRIPT(
        RELEASE_REPO, "1.6.1", "TypeScriptAnalyser",
    ),
    JAVASCRIPT(
        TYPESCRIPT.url, TYPESCRIPT.version, TYPESCRIPT.className
    ),
    APICALLS(
        RELEASE_REPO, "1.6.1", "ApiCallAnalyser", false,
    ),
    DATAMAP(
        RELEASE_REPO, "1.6.1", "DataMapAnalyser", false,
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
