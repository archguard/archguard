package org.archguard.threshold

data class CircularDependenciesCount(
    val systemId: Long,
    val moduleCircularDependenciesCount: Int,
    val packageCircularDependenciesCount: Int,
    val classCircularDependenciesCount: Int,
    val methodCircularDependenciesCount: Int
)
