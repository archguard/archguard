package org.archguard.codedb.fitness

import org.jetbrains.kotlinx.jupyter.api.libraries.LibraryDefinition
import org.jetbrains.kotlinx.jupyter.api.libraries.LibraryReference
import org.jetbrains.kotlinx.jupyter.api.libraries.Variable
import org.jetbrains.kotlinx.jupyter.libraries.AbstractLibraryResolutionInfo
import org.jetbrains.kotlinx.jupyter.libraries.ChainedLibraryResolver
import org.jetbrains.kotlinx.jupyter.libraries.LibraryDescriptor
import org.jetbrains.kotlinx.jupyter.libraries.LibraryResolver
import org.jetbrains.kotlinx.jupyter.libraries.parseLibraryDescriptors

fun Collection<Pair<String, String>>.toLibraries(): LibraryResolver {
    val libJsons = associate { it.first to it.second }
    return getResolverFromNamesMap(parseLibraryDescriptors(libJsons))
}

fun getResolverFromNamesMap(
    descriptors: Map<String, LibraryDescriptor>? = null,
    definitions: Map<String, LibraryDefinition>? = null,
): LibraryResolver {
    return ArchGuardLibraryResolver(
        null,
        descriptors?.mapKeys { entry -> LibraryReference(AbstractLibraryResolutionInfo.Default(), entry.key) },
        definitions?.mapKeys { entry -> LibraryReference(AbstractLibraryResolutionInfo.Default(), entry.key) },
    )
}

class ArchGuardLibraryResolver(
    parent: LibraryResolver?,
    initialDescriptorsCache: Map<LibraryReference, LibraryDescriptor>? = null,
    initialDefinitionsCache: Map<LibraryReference, LibraryDefinition>? = null,
) : ChainedLibraryResolver(parent) {
    private val definitionsCache = hashMapOf<LibraryReference, LibraryDefinition>()
    private val descriptorsCache = hashMapOf<LibraryReference, LibraryDescriptor>()

    init {
        initialDescriptorsCache?.forEach { (key, value) ->
            descriptorsCache[key] = value
        }
        initialDefinitionsCache?.forEach { (key, value) ->
            definitionsCache[key] = value
        }
    }

    override fun shouldResolve(reference: LibraryReference): Boolean {
        return reference.shouldBeCachedInMemory
    }

    override fun tryResolve(reference: LibraryReference, arguments: List<Variable>): LibraryDefinition? {
        return definitionsCache[reference] ?: descriptorsCache[reference]?.convertToDefinition(arguments)
    }

    override fun save(reference: LibraryReference, definition: LibraryDefinition) {
        definitionsCache[reference] = definition
    }
}
