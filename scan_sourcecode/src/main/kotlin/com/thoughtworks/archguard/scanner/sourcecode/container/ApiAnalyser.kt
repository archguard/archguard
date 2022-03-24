package com.thoughtworks.archguard.scanner.sourcecode.container

import chapi.domain.core.CodeDataStruct
import com.thoughtworks.archguard.scanner.sourcecode.frontend.ContainerService

interface ApiAnalyser {
    fun analysis(node: CodeDataStruct, path: String): Array<ContainerService>
}