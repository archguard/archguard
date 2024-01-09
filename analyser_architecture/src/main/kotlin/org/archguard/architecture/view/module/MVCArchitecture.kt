package org.archguard.architecture.view.module

import org.archguard.architecture.view.module.shared.Dependency

class MVCArchitecture(
    val models: List<Model>,
    val views: List<View>,
    val controllers: List<Controller>,
    val dependencies: List<Dependency>
) :
    ArchitectureStyle

class Model

class View

class Controller
