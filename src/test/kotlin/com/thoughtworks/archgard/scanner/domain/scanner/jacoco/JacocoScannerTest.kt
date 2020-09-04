package com.thoughtworks.archgard.scanner.domain.scanner.jacoco

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.system.BuildTool
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.io.File

@SpringBootTest
@ActiveProfiles("test")
internal class JacocoScannerTest(@Autowired val jacocoScanner: JacocoScanner, @Autowired val jdbi: Jdbi) {
    @Test
    fun should_get_jacoco_report() {
        jacocoScanner.scan(ScanContext(1, "", BuildTool.MAVEN, File(javaClass.classLoader.getResource("TestJacoco").toURI()), "",ArrayList()))
        val bundle = jdbi.withHandle<Int, RuntimeException> { handle: Handle ->
            handle.createQuery("select count(*) from bundle")
                    .mapTo(Int::class.java).one()
        }
        assertEquals(bundle, 2)
        val item = jdbi.withHandle<Int, RuntimeException> { handle: Handle ->
            handle.createQuery("select count(*) from item")
                    .mapTo(Int::class.java).one()
        }
        assertEquals(item, 87)
    }
}