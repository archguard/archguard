package org.archguard.architecture.core

import chapi.domain.core.CodeDataStruct
import org.archguard.architecture.ArchitectureView
import org.archguard.scanner.core.sca.PackageDependencies
import org.archguard.scanner.core.sourcecode.ContainerService

/**
 * **Workspace** is like IDE/Editor's workspace, same as to Git/SVN project.
 *
 * @property dataStructs the analysis result of projects.
 * @property projectDependencies the analysis result of package manager's config.
 * @property service the analysis result of container services.
 *
 */
class Workspace(
    val dataStructs: List<CodeDataStruct> = listOf(),
    val projectDependencies: List<PackageDependencies> = listOf(),
    val service: List<ContainerService> = listOf(),
) {
    fun analysis(): ArchitectureView {
        return ArchitectureView()
    }
}
