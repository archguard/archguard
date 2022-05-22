package org.archguard.architecture.core.module.view.module

class MVCArchitecture(val models: List<Model>, val views : List<View>, val controllers: List<Controller>, val dependencies: List<Dependency>) : ModuleArchitecture

class Model

class View

class Controller

class Dependency(val type: DependencyType, val dependent: String, val dependence: String)

enum class DependencyType {
    Notify,
    Query,
    Call,
    Update
}
