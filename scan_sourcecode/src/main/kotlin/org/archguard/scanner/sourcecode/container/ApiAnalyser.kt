package org.archguard.scanner.sourcecode.container

import chapi.domain.core.CodeDataStruct

interface ApiAnalyser {
    fun analysis(node: CodeDataStruct, path: String): Array<ContainerService>
}