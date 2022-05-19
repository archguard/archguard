package org.archguard.architecture.core.module.view.module

/**
 * we assume a module in maven or gradle should be a subsystem.
 * So in module architecture have one or more layers.
 */
class LayeredArchitecture(val layers: List<MLayer>) : ModuleArchitecture
