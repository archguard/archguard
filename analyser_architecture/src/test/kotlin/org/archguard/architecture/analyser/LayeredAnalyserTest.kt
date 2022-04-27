package org.archguard.architecture.analyser

import chapi.domain.core.CodeDataStruct
import org.junit.jupiter.api.Test

internal class LayeredAnalyserTest {
    @Test
    internal fun mvc() {
        LayeredAnalyser(LayeredContext()).analyse(
            listOf(
                CodeDataStruct(NodeName = "HelloController"),
                CodeDataStruct(NodeName = "HelloService"),
                CodeDataStruct(NodeName = "HelloRepository"),
            )
        )
    }
}