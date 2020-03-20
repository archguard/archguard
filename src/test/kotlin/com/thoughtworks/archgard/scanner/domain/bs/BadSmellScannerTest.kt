package com.thoughtworks.archgard.scanner.domain.bs

import com.thoughtworks.archgard.scanner.domain.ScanContext
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.io.File

@SpringBootTest
@ActiveProfiles("test")
internal class BadSmellScannerTest(@Autowired val badSmellScanner: BadSmellScanner, @Autowired val jdbi: Jdbi) {
    @Test
    fun should_get_bad_smell_report() {
        val scanContext = ScanContext()
        scanContext.projectRoot = File(javaClass.classLoader.getResource("TestProject").toURI())
        badSmellScanner.scan(scanContext)

        val count = jdbi.withHandle<Int, RuntimeException> { handle: Handle ->
            handle.createQuery("select count(*) from badSmell")
                    .mapTo(Int::class.java).one()
        }
        Assertions.assertEquals(count, 2)
    }
}