package com.thoughtworks.archguard.scanner.domain.scanner.javaext.bs

import com.thoughtworks.archguard.scanner.domain.system.BuildTool
import com.thoughtworks.archguard.scanner.infrastructure.command.InMemoryConsumer
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.io.File

@SpringBootTest
@ActiveProfiles("test")
@Disabled
internal class BadSmellScannerTest(@Autowired val badSmellScanner: BadSmellScanner, @Autowired val jdbi: Jdbi) {
    @Test
    fun should_get_bad_smell_report() {
        val scanContext = ScanContext(
            1,
            "repo",
            BuildTool.GRADLE,
            File(javaClass.classLoader.getResource("TestProject").toURI()),
            "",
            ArrayList(),
            "jvm",
            "",
            "",
            InMemoryConsumer(),
            listOf()
        )
        badSmellScanner.scan(scanContext)

        val badSmells = jdbi.withHandle<List<BadSmell>, RuntimeException> { handle: Handle ->
            handle.createQuery("select * from metric_code_bad_smell")
                    .mapTo(BadSmell::class.java).list()
        }
        Assertions.assertEquals(badSmells.size, 2)
        Assertions.assertEquals(badSmells[0].systemId, 1)
    }
}