package org.archguard.scanner.architecture.view.module.layer

import kotlinx.serialization.Serializable


/**
 * Layer in Module Level, one layer should contain one or more components
 */
@Serializable
class MLayer(val name: String, val components: List<MComponent>)
