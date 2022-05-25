package org.archguard.architecture.core.module.view.module

interface ModuleArchitecture

class Dependency(val type: DependencyType, val dependent: String, val dependence: String)

enum class DependencyType {
    Notify,
    Query,
    Call,
    Update
}