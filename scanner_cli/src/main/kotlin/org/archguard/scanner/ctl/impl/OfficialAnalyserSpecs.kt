package org.archguard.scanner.ctl.impl

import org.archguard.scanner.core.AnalyserSpec

private const val VERSION = "1.7.0"
private const val TAG = "v$VERSION"
private const val RELEASE_REPO_URL = "https://github.com/archguard/scanner/releases/download/$TAG"

enum class OfficialAnalyserSpecs(
    private val className: String,
    private val isLanguage: Boolean = true,
) {
    // languages
    CSHARP("CSharpAnalyser"),
    GO("GoAnalyser"),
    JAVA("JavaAnalyser"),
    KOTLIN("KotlinAnalyser"),
    PYTHON("PythonAnalyser"),
    SCALA("ScalaAnalyser"),
    TYPESCRIPT("TypeScriptAnalyser"),
    JAVASCRIPT(TYPESCRIPT.className),

    // features
    APICALLS("ApiCallAnalyser", false),
    DATAMAP("DataMapAnalyser", false),

    // git
    GIT("GitAnalyser", false),
    ;

    fun spec(): AnalyserSpec {
        if (this == GIT) return AnalyserSpec(
            identifier = "git",
            host = RELEASE_REPO_URL,
            version = VERSION,
            jar = "analyser_git-$VERSION-all.jar",
            className = "GitAnalyser"
        )

        val identifier = name.lowercase()
        val prefix = if (isLanguage) "lang" else "feat"
        val jar = "${prefix}_$identifier-$VERSION-all.jar"

        return AnalyserSpec(identifier, RELEASE_REPO_URL, VERSION, jar, className)
    }

    companion object {
        fun specs() = values().map(OfficialAnalyserSpecs::spec)
    }
}
