package com.thoughtworks.archgard.scanner.domain.toolscanners

import com.thoughtworks.archgard.scanner.domain.ScanContext
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.File
import java.net.URL

@SpringBootTest
internal class CocaScannerTest {
    @Autowired
    private lateinit var cocaScanner: CocaScanner

    @Test
    fun should_get_coca_report() {
        cocaScanner.latestCocaUrl = URL("http://ci.archguard.org/view/ThirdPartyTool/job/coca/lastSuccessfulBuild/artifact/coca_macos")
        val root = File(javaClass.classLoader.getResource("TestProject").toURI())
        val scanContext = ScanContext(root)
        cocaScanner.preProcess(scanContext)
        cocaScanner.scan(scanContext)
        cocaScanner.storeReport(scanContext)
    }
}