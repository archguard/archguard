package org.archguard.scanner.analyser.frontend.identify

import chapi.domain.core.CodeCall
import chapi.domain.core.CodeImport
import org.archguard.scanner.core.client.dto.ContainerDemand

interface HttpIdentify {
    fun isMatch(call: CodeCall, imports: Array<CodeImport>): Boolean
    fun convert(call: CodeCall): ContainerDemand
}
