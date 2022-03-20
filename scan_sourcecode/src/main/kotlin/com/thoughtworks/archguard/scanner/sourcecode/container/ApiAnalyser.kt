package com.thoughtworks.archguard.scanner.sourcecode.container

import chapi.domain.core.CodeDataStruct

interface ApiAnalyser {
    fun analysis(node: CodeDataStruct, path: String)
}