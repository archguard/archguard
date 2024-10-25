package org.archguard.scanner.analyser

import chapi.domain.core.CodeDataStruct
import org.archguard.scanner.architecture.ArchitectureView
import org.archguard.scanner.architecture.PhysicalStructure
import org.archguard.scanner.architecture.view.concept.DomainModel
import org.archguard.scanner.architecture.view.module.ArchitectureStyle
import org.archguard.model.PackageDependencies
import org.archguard.model.ContainerService

/**
 * Workspace is like IDE/Editor's workspace, same as to project.
 *
 * @property dataStructs the analysis result of projects.
 * @property projectDependencies the analysis result of package manager's config.
 * @property service the analysis result of container services.
 *
 */
class WorkspaceAnaylser(
    val dataStructs: List<CodeDataStruct> = listOf(),
    val projectDependencies: List<PackageDependencies> = listOf(),
    val service: List<ContainerService> = listOf(),
    val language: String = "Java",
) {
    fun analysis(): ArchitectureView {
        val identPotential = ArchitectureDetect().identPotential(this)

        return ArchitectureView(
            conceptArchitecture = org.archguard.scanner.architecture.view.concept.ConceptArchitecture(
                domainModels = DomainModel.from(identPotential.concepts)
            ),
            outboundService = service,
            architectureStyle = ArchitectureStyle.from(identPotential),
            physicalStructure = PhysicalStructure(
                languageEstimate = listOf(),
                codeStructure = identPotential.physicalStructure,
            )
        )
    }
}
