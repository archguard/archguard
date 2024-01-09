package org.archguard.smell

/**
 * The `BadSmellLevelType` enum class represents the different levels of bad smell in a codebase.
 * It provides four possible levels: A, B, C, and D.
 *
 * The levels are defined as follows:
 * - Level A represents a high level of bad smell, indicating that the codebase requires immediate attention and refactoring.
 * - Level B represents a moderate level of bad smell, indicating that there are some areas in the codebase that need improvement.
 * - Level C represents a low level of bad smell, indicating that there are a few minor issues in the codebase that can be addressed.
 * - Level D represents a very low level of bad smell, indicating that the codebase is relatively clean and well-maintained.
 *
 * This enum class is useful for categorizing and prioritizing bad smells in a codebase, allowing developers to focus on the most critical areas for improvement.
 *
 * Example usage:
 * ```
 * val smellLevel = BadSmellLevelType.A
 * when (smellLevel) {
 *     BadSmellLevelType.A -> println("High level of bad smell, requires immediate attention.")
 *     BadSmellLevelType.B -> println("Moderate level of bad smell, some areas need improvement.")
 *     BadSmellLevelType.C -> println("Low level of bad smell, a few minor issues can be addressed.")
 *     BadSmellLevelType.D -> println("Very low level of bad smell, codebase is relatively clean.")
 * }
 * ```
 */
enum class BadSmellLevelType {
    A, B, C, D
}
