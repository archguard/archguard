package org.archguard.architecture.core

import chapi.domain.core.CodeDataStruct
import org.archguard.analyser.sca.model.PackageDependencies
import org.archguard.scanner.core.client.dto.ContainerService

class Workspace(
    val dataStructs: List<CodeDataStruct>,
    val projectDependencies: PackageDependencies,
    val services: ContainerService,
//    val lan
)
