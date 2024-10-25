package org.archguard.scanner.architecture.view.module.shared

import kotlinx.serialization.Serializable

@Serializable
class Dependency(val type: DependencyType, val dependent: String, val dependence: String)