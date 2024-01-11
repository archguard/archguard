package org.archguard.architecture.view.module

import kotlinx.serialization.Serializable
import org.archguard.architecture.detect.PotentialExecArch

/**
 * The `MicroKernelArchitecture` class is a concrete implementation of the `ModuleArchitecture` interface.
 * It represents a microkernel architecture, which is a software architecture pattern that decomposes a system into
 * small, loosely-coupled modules that communicate through a central kernel.
 *
 * This class is annotated with `@Serializable` to indicate that instances of this class can be serialized and
 * deserialized using Kotlin serialization.
 *
 * @see ArchitectureStyle
 *
 * @constructor Creates a new instance of the `MicroKernelArchitecture` class.
 */
@Serializable
class MicroKernelArchitecture : ArchitectureStyle {
    override fun canBeApplied(identPotential: PotentialExecArch): Boolean {
        return false
    }
}