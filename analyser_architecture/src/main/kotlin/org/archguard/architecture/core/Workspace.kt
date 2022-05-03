package org.archguard.architecture.core

import chapi.domain.core.CodeDataStruct
import org.archguard.analyser.sca.model.PackageDependencies
import org.archguard.scanner.core.client.dto.CodeLanguage
import org.archguard.scanner.core.client.dto.ContainerService

class Workspace(
    val dataStructs: List<CodeDataStruct> = listOf(),
    val projectDependencies: PackageDependencies = PackageDependencies("", "", "", listOf()),
    val outbounds: ContainerService = ContainerService(),
    val languages: List<CodeLanguage> = listOf()
)
