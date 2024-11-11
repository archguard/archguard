package org.archguard.scanner.analyser

import chapi.domain.core.CodeDataStruct
import org.archguard.architecture.ArchitectureView
import org.archguard.architecture.PhysicalStructure
import org.archguard.architecture.view.code.CodeArchitecture
import org.archguard.architecture.view.code.RepositoryType
import org.archguard.architecture.view.concept.ConceptArchitecture
import org.archguard.architecture.view.concept.DomainModel
import org.archguard.architecture.view.module.ArchitectureStyle
import org.archguard.context.ContainerService
import org.archguard.context.PackageDependencies

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
    fun analysis(workspace: String): ArchitectureView {
        val identPotential = ArchitectureDetect().identPotential(this)

        val domainModels = DomainModel.from(identPotential.concepts, workspace)
        return ArchitectureView(
            conceptArchitecture = ConceptArchitecture(
                domainModels = domainModels
            ),
            outboundService = service,
            architectureStyle = ArchitectureStyle.from(identPotential),
            codeArchitecture = CodeArchitecture(
                RepositoryType.Service,
                stacks = identPotential.coreStacks
            ),
            physicalStructure = PhysicalStructure(
                languageEstimate = listOf(),
                codeStructure = identPotential.physicalStructure,
            )
        )
    }
}
