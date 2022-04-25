package org.archguard.scanner.bytecode

import org.archguard.scanner.analyser.backend.JavaApiAnalyser
import org.junit.jupiter.api.Test
import java.nio.file.Paths
import kotlin.test.assertEquals

class ApiParserTest {
    @Test
    fun should_ident_demand_source() {
        val resource = this.javaClass.classLoader.getResource("kotlin/QualityGateClientImpl.class")
        val path = Paths.get(resource.toURI()).toFile()
        val ds = ByteCodeParser().parseClassFile(path)

        val javaApiAnalyser = JavaApiAnalyser()
        javaApiAnalyser.analysisByNode(ds, "")
        val services = javaApiAnalyser.toContainerServices()
        assertEquals(1, services[0].demands.size)
        assertEquals("QualityGateClientImpl", services[0].demands[0].source_caller)
    }
}
