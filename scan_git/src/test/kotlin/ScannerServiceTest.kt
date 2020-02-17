import com.thoughtworks.archguard.git.scanner.Config
import com.thoughtworks.archguard.git.scanner.Main
import com.thoughtworks.archguard.git.scanner.ScannerService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [Main::class])
internal class ScannerServiceTest(
        @Autowired val service: ScannerService) {

    @Test
    internal fun `to scan specified git repository`() {
        service.scan(Config("test_data"))
    }

    @Test
    internal fun `get commit history`() {
        val commitHistory = service.get()
        assertEquals("master", commitHistory.branch)
        assertEquals(3, commitHistory.commits!!.size)
    }
}