package org.archguard.scanner.ctl.impl

import org.archguard.scanner.core.AnalyserSpec

const val ARCHGUARD_VERSION = "2.0.2"
private const val TAG = "v$ARCHGUARD_VERSION"
private const val RELEASE_REPO_URL = "https://github.com/archguard/archguard/releases/download/$TAG"

enum class OfficialAnalyserSpecs(
    private val className: String,
) {
    // languages
    CSHARP("CSharpAnalyser"),
    GOLANG("GoAnalyser"),
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
    RULE("Rule"),
    DIFF_CHANGES("DiffChangesAnalyser"),
    ESTIMATE("EstimateAnalyser"),
    ;

    fun spec() = AnalyserSpec(identifier(), RELEASE_REPO_URL, ARCHGUARD_VERSION, jarFileName(), className)
    fun version() = ARCHGUARD_VERSION
    fun identifier(): String {
        return if (name.lowercase() == "javascript") {
            // we use the same analyser for javascript and typescript
            "typescript"
        } else {
            name.lowercase()
        }
    }

    fun jarFileName(): String {
        val identifier = identifier()
        val prefix = when (this) {
            GIT, SCA, DIFF_CHANGES, ESTIMATE -> "analyser"
            DATAMAP, APICALLS -> "feat"
            RULE -> "rule"
            else -> "lang"
        }
        return "${prefix}_$identifier-$ARCHGUARD_VERSION-all.jar"
    }

    companion object {
        fun specs() = values().map(OfficialAnalyserSpecs::spec)
        fun host() = RELEASE_REPO_URL
    }
}
