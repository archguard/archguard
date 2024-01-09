package org.archguard.architecture.view.module.mvc

import org.archguard.architecture.view.module.Dependency

class MVCArchitecture(
    val models: List<Model>,
    val views: List<View>,
    val controllers: List<Controller>,
    val dependencies: List<Dependency>
) :
    org.archguard.architecture.view.module.ModuleArchitecture

class Model

class View

class Controller
