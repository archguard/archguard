import com.thoughtworks.archguard.git.analyzer.Runner
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [Runner::class])
class CommonTest {
    @Test
    internal fun testJupiterConfig() {
        assertTrue(true)
    }


}