package org.archguard.scanner.analyser

import chapi.domain.core.CodeDataStruct
import org.archguard.architecture.ArchitectureView
import org.archguard.architecture.PhysicalStructure
import org.archguard.architecture.view.concept.ConceptArchitecture
import org.archguard.architecture.view.concept.DomainModel
import org.archguard.architecture.view.module.ArchitectureStyle
import org.archguard.model.ContainerService
import org.archguard.model.PackageDependencies

/**
 * Workspace is like IDE/Editor's workspace, same as to project.
 *
 * @property dataStructs the analysis result of projects.
 * @property projectDependencies the analysis result of package manager's config.
 * @property service the analysis result of container services.
 *
 */
class WorkspaceAnalyser(
    val dataStructs: List<CodeDataStruct> = listOf(),
    val projectDependencies: List<PackageDependencies> = listOf(),
    val service: List<ContainerService> = listOf(),
    val language: String = "Java",
) {
    fun analysis(): ArchitectureView {
        val identPotential = ArchitectureDetect().identPotential(this)

        return ArchitectureView(
            conceptArchitecture = ConceptArchitecture(
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
