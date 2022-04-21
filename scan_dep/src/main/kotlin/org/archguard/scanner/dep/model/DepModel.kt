package org.archguard.scanner.dep.model

/*
 * file for save content ?
 */
class DepFile(
    val name: String,
    val content: String,
)

class DepModel(
    // self name
    val name: String,
    // self version for some cases
    val version: String,
    // like `maven`, `gradle`
    val packageManager: String,
    // requirements in maven
    val dependencies: List<DepDependency>
) {
}

class DepDependency(
    // version
    val version: String,
    val file: String,
    val group: List<String>,
    // url: like github, maven
    // file: like NPM in local
    val source: DepSource,
    // additional information
    val metadata: DepMetadata
) {

}

// like GitHub link,
class DepSource (
    val type: String,
    val url: String,
    val branch: String,
    val ref: String,
)

class DepMetadata(
    val packagingType: String,
    val propertyName: String,
)