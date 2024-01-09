package org.archguard.architecture.view.module.mvc

import org.archguard.architecture.view.module.Dependency
import org.archguard.architecture.view.module.ModuleArchitecture

class MVCArchitecture(
    val models: List<org.archguard.architecture.view.module.mvc.Model>,
    val views: List<org.archguard.architecture.view.module.mvc.View>,
    val controllers: List<org.archguard.architecture.view.module.mvc.Controller>,
    val dependencies: List<org.archguard.architecture.view.module.Dependency>
) :
    org.archguard.architecture.view.module.ModuleArchitecture

class Model

class View

class Controller
