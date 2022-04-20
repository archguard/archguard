package org.archguard.scanner.dep.model

class DepFile(
    val name: String,
    val content: String,
)

class DepModel(
    val name: String,
    val version: String,
    val packageManager: String,
    // todo: change to version
    val requirements: List<DepRequirement>
) {
}

class DepRequirement(
    // version
    val requirement: String,
    val file: String,
    val group: List<String>,
    val source: DepSource,
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