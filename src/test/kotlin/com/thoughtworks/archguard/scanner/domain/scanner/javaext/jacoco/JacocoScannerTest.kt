package com.thoughtworks.archguard.scanner.domain.scanner.javaext.jacoco

import com.thoughtworks.archguard.scanner.domain.scanner.javaext.bs.ScanContext
import com.thoughtworks.archguard.scanner.domain.system.BuildTool
import com.thoughtworks.archguard.scanner.infrastructure.command.InMemoryConsumer
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.io.File

@SpringBootTest
@ActiveProfiles("test")
@Disabled
internal class JacocoScannerTest(@Autowired val jacocoScanner: JacocoScanner, @Autowired val jdbi: Jdbi) {
    @Test
    fun should_get_jacoco_report() {
        jacocoScanner.scan(
            ScanContext(
                1,
                "",
                BuildTool.MAVEN,
                File(javaClass.classLoader.getResource("TestJacoco").toURI()),
                "",
                ArrayList(),
                "jvm",
                "",
                "",
                InMemoryConsumer(),
                listOf()
            )
        )
        val bundle = jdbi.withHandle<Int, RuntimeException> { handle: Handle ->
            handle.createQuery("select count(*) from test_coverage_bundle")
                    .mapTo(Int::class.java).one()
        }
        assertEquals(2, bundle)
        val item = jdbi.withHandle<Int, RuntimeException> { handle: Handle ->
            handle.createQuery("select count(*) from test_coverage_item")
                    .mapTo(Int::class.java).one()
        }
        assertEquals(87, item)
    }
}