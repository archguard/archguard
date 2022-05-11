package org.archguard.architecture.core

import chapi.domain.core.CodeDataStruct
import org.archguard.scanner.core.sca.PackageDependencies
import org.archguard.scanner.core.archtecture.CodeLanguage
import org.archguard.scanner.core.sourcecode.ContainerService

/**
 * **Workspace** is like IDE/Editor's workspace, same as to Git/SVN project.
 *
 * @property dataStructs the analysis result of projects.
 * @property projectDependencies the analysis result of package manager's config.
 * @property port the analysis result of container services.
 * @property languages the overview of project languages.
 *
 */
class Workspace(
    val dataStructs: List<CodeDataStruct> = listOf(),
    val projectDependencies: PackageDependencies = PackageDependencies("", "", "", listOf()),
    val port: ContainerService = ContainerService(),
    val languages: List<CodeLanguage> = listOf()
)
