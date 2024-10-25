@file:Suppress("UnstableApiUsage")

enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "Archguard"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        mavenLocal()
    }
}

// meta type
include(":meta")

// backend
include(":server")
include(":server:metric-service")

include(":doc-generator")

/**
 * architecture as code repos
 */

include(
    ":architecture-as-code:domain",
    ":architecture-as-code:dsl",
    ":architecture-as-code:repl-api"
)

/**
 *  scanner projects:
 * scanner projects:
 * - core: define the core models and apis
 * - cli: executable command line tools to manage and choregraph
 */
include(
    ":scanner_core",
    ":scanner_cli",
    // source code
    ":analyser_sourcecode:lang_kotlin",
    ":analyser_sourcecode:lang_java",
    ":analyser_sourcecode:lang_typescript",
    ":analyser_sourcecode:lang_python",
    ":analyser_sourcecode:lang_golang",
    ":analyser_sourcecode:lang_csharp",
    ":analyser_sourcecode:lang_scala",
    ":analyser_sourcecode:lang_rust",
    ":analyser_sourcecode:lang_c",
    ":analyser_sourcecode:lang_cpp",

    // idl, Interface description language
    ":analyser_sourcecode:idl_protobuf",

    // features
    ":analyser_sourcecode:feat_apicalls",
    ":analyser_sourcecode:feat_datamap",

    // external tools
    ":analyser_git",
    ":analyser_estimate",
    ":analyser_diff_changes",
    ":analyser_sca",
    ":analyser_architecture",
    ":analyser_openapi",
    ":analyser_document",
)

/**
 * linters projects: a specific set of analysers to detect specific patterns
 */
include(
    ":rule-core",
    ":rule-linter:rule-sql",
    ":rule-linter:rule-test",
    ":rule-linter:rule-webapi",
    ":rule-linter:rule-code",
    ":rule-linter:rule-layer",
    ":rule-linter:rule-comment",
)
