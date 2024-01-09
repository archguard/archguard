package org.archguard.threshold

/**
 * Represents the count of circular dependencies in a system.
 *
 * @property systemId The ID of the system.
 * @property moduleCircularDependenciesCount The count of circular dependencies at the module level.
 * @property packageCircularDependenciesCount The count of circular dependencies at the package level.
 * @property classCircularDependenciesCount The count of circular dependencies at the class level.
 * @property methodCircularDependenciesCount The count of circular dependencies at the method level.
 */
data class CircularDependenciesCount(
    val systemId: Long,
    val moduleCircularDependenciesCount: Int,
    val packageCircularDependenciesCount: Int,
    val classCircularDependenciesCount: Int,
    val methodCircularDependenciesCount: Int
)
