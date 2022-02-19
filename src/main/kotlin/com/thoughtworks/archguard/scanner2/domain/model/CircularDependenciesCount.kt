package com.thoughtworks.archguard.scanner2.domain.model

data class CircularDependenciesCount(val systemId: Long, val moduleCircularDependenciesCount: Int, val packageCircularDependenciesCount: Int,
                                val classCircularDependenciesCount: Int, val methodCircularDependenciesCount: Int)