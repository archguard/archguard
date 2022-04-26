package org.archguard.scanner.ctl.impl

import org.archguard.scanner.core.AnalyserSpec

private const val VERSION = "1.6.3"
private const val TAG = "plugin-ea-0.0.4"
private const val RELEASE_REPO_URL = "https://github.com/archguard/scanner/releases/download/$TAG"

enum class OfficialAnalyserSpecs(
    private val url: String,
    private val version: String,
    private val className: String,
    private val isLanguage: Boolean = true,
) {
    CSHARP(
        RELEASE_REPO_URL, VERSION, "CSharpAnalyser",
    ),
    GO(
        RELEASE_REPO_URL, VERSION, "GoAnalyser",
    ),
    JAVA(
        RELEASE_REPO_URL, VERSION, "JavaAnalyser",
    ),
    KOTLIN(
        RELEASE_REPO_URL, VERSION, "KotlinAnalyser",
    ),
    PYTHON(
        RELEASE_REPO_URL, VERSION, "PythonAnalyser",
    ),
    SCALA(
        RELEASE_REPO_URL, VERSION, "ScalaAnalyser",
    ),
    TYPESCRIPT(
        RELEASE_REPO_URL, VERSION, "TypeScriptAnalyser",
    ),
    JAVASCRIPT(
        TYPESCRIPT.url, TYPESCRIPT.version, TYPESCRIPT.className
    ),
    APICALLS(
        RELEASE_REPO_URL, VERSION, "ApiCallAnalyser", false,
    ),
    DATAMAP(
        RELEASE_REPO_URL, VERSION, "DataMapAnalyser", false,
    ),
    ;

    fun spec(): AnalyserSpec {
        val identifier = name.lowercase()
        val prefix = if (isLanguage) "lang" else "feat"
        val jar = "${prefix}_$identifier-$version-all.jar"

        return AnalyserSpec(identifier, url, version, jar, className)
    }

    companion object {
        fun specs() = values().map(OfficialAnalyserSpecs::spec)
    }
}

// https://github.com/archguard/scanner/releases/download/plugin-ea-0.0.4/lang_kotlin-1.6.3-all.jar
// https://github.com/archguard/scanner/releases/download/plugin-ea-0.0.4/lang_kotlin-1.6.3-all.jar
// https://github.com/archguard/scanner/releases/download/plugin-ea-0.0.4/1.6.3/lang_kotlin-1.6.3-all.jar
