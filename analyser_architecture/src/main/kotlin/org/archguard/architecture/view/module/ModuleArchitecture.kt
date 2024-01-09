package org.archguard.architecture.view.module

interface ModuleArchitecture

class Dependency(val type: org.archguard.architecture.view.module.DependencyType, val dependent: String, val dependence: String)

enum class DependencyType {
    Notify,
    Query,
    Call,
    Update
}