package org.archguard.scanner.common.container

import chapi.domain.core.CodeDataStruct

interface ApiAnalyser {
    fun analysis(node: CodeDataStruct, path: String): Array<ContainerService>
}