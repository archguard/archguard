package org.archguard.scanner.ctl.impl

import org.archguard.scanner.core.AnalyserSpec

private const val VERSION = "2.0.0-alpha.16"
private const val TAG = "v$VERSION"
private const val RELEASE_REPO_URL = "https://github.com/archguard/archguard/releases/download/$TAG"

enum class OfficialAnalyserSpecs(
    private val className: String,
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
    APICALLS("ApiCallAnalyser"),
    DATAMAP("DataMapAnalyser"),

    GIT("GitAnalyser"),
    SCA("ScaAnalyser"),
    Rule("Rule"),
    DIFF_CHANGES("DiffChangesAnalyser"),
    ;

    fun spec() = AnalyserSpec(identifier(), RELEASE_REPO_URL, VERSION, jarFileName(), className)
    fun version() = VERSION

    private fun identifier() = name.lowercase()
    private fun jarFileName(): String {
        val identifier = identifier()
        val prefix = when (this) {
            GIT, SCA, DIFF_CHANGES -> "analyser"
            DATAMAP, APICALLS -> "feat"
            Rule -> "rule"
            else -> "lang"
        }
        return "${prefix}_$identifier-$VERSION-all.jar"
    }

    companion object {
        fun specs() = values().map(OfficialAnalyserSpecs::spec)
    }
}
