package org.archguard.architecture.core

/**
 * The `DevelMethodology` enum class represents different development methodologies used in software development.
 * It provides three options: Agile, Waterfall, and Unknown.
 *
 * - `Agile`: Represents the Agile development methodology where frequent releases are made using tags.
 * - `Waterfall`: Represents the Waterfall development methodology where there are no tags.
 * - `Unknown`: Represents an unknown or unspecified development methodology.
 *
 * This enum class is used to categorize and identify the development methodology being used in a software project.
 * It can be used to make decisions or perform actions based on the specific methodology being followed.
 *
 * Can be analysis by all tags.
 */
enum class DevelMethodology {
    Agile,
    Waterfall,
    Unknown
}