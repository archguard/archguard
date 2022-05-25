package org.archguard.architecture.core.module.view.module.layer

import org.archguard.architecture.core.module.view.module.ModuleArchitecture

/**
 * we assume a module in maven or gradle should be a subsystem.
 * So in module architecture have one or more layers.
 */
class LayeredArchitecture(val layers: List<MLayer>) : ModuleArchitecture
