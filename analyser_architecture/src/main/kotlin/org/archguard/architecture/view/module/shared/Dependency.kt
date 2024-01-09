package org.archguard.architecture.view.module.shared

import kotlinx.serialization.Serializable
import org.archguard.architecture.view.module.DependencyType

@Serializable
class Dependency(val type: DependencyType, val dependent: String, val dependence: String)