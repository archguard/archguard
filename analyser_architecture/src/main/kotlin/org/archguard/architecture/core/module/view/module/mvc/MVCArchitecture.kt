package org.archguard.architecture.core.module.view.module.mvc

import org.archguard.architecture.core.module.view.module.Dependency
import org.archguard.architecture.core.module.view.module.ModuleArchitecture

class MVCArchitecture(
    val models: List<Model>,
    val views: List<View>,
    val controllers: List<Controller>,
    val dependencies: List<Dependency>
) :
    ModuleArchitecture

class Model

class View

class Controller
