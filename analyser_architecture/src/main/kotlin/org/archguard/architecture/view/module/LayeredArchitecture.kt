package org.archguard.architecture.view.module

import kotlinx.serialization.Serializable
import org.archguard.architecture.view.module.layer.MLayer

/**
 * we assume a module in maven or gradle should be a subsystem.
 * So in module architecture have one or more layers.
 */
@Serializable
class LayeredArchitecture(val layers: List<MLayer>) : ModuleArchitecture
