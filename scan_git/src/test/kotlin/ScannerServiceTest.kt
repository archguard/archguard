import com.thoughtworks.archguard.git.scanner.Config
import com.thoughtworks.archguard.git.scanner.Main
import com.thoughtworks.archguard.git.scanner.ScannerService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [Main::class])
internal class ScannerServiceTest(
        @Autowired val service: ScannerService) {


    @Test
    internal fun runTest() {
        service.git2SqlFile(Config("/Users/ygdong/git/junit4_demo"))
    }
}