package org.archguard.architecture.view.module

import kotlinx.serialization.Serializable

@Serializable
sealed interface ModuleArchitecture

enum class DependencyType {
    Notify,
    Query,
    Call,
    Update
}