import com.thoughtworks.archguard.git.analyzer.GitAnalyzer
import com.thoughtworks.archguard.git.analyzer.Runner
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [Runner::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GitAnalyzerTest {

    @Autowired
    lateinit var analyzer: GitAnalyzer

    @Test
    internal fun testAnalyzer() {
        val findScatterCommits = analyzer.findScatterCommits()
        assertEquals(1, findScatterCommits.size)
    }
}