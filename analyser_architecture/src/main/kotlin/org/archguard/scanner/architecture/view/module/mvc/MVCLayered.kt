package org.archguard.scanner.architecture.view.module.mvc

import kotlinx.serialization.Serializable

@Serializable
data class Model(val name: String)

@Serializable
data class View(val name: String)

@Serializable
data class Controller(val name: String)