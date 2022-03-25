package com.thoughtworks.archguard.scanner.domain.scanner.javaext.tbs

import com.thoughtworks.archguard.scanner.domain.scanner.javaext.bs.ScanContext
import com.thoughtworks.archguard.scanner.domain.system.BuildTool
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
internal class TestBadSmellScannerTest(@Autowired val testBadSmellScanner: TestBadSmellScanner, @Autowired val jdbi: Jdbi) {

    @Test
    fun should_get_test_bad_smell_report() {
        val scanContext = ScanContext(
            1,
            "repo",
            BuildTool.GRADLE,
            File(javaClass.classLoader.getResource("TestBadSmell").toURI()),
            "",
            ArrayList(),
            "jvm",
            "",
            ""
        )
        testBadSmellScanner.scan(scanContext)

        val testBadSmells = jdbi.withHandle<List<TestBadSmell>, RuntimeException> { handle: Handle ->
            handle.createQuery("select * from metric_test_bad_smell")
                    .mapTo(TestBadSmell::class.java).list()
        }
        assertEquals(12, testBadSmells.size)
        assertEquals(1, testBadSmells[0].systemId)

        val testCount = jdbi.withHandle<Int, RuntimeException> { handle: Handle ->
            handle.createQuery("select overview_value from system_overview where overview_type='test'")
                    .mapTo(Int::class.java).one()
        }

        assertEquals(1, testCount)

    }

}
