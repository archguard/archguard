package org.archguard.architecture.view.module.shared

import kotlinx.serialization.Serializable

@Serializable
class Dependency(val type: DependencyType, val dependent: String, val dependence: String)