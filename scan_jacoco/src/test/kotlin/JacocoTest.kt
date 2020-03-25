import com.thoghtworks.archguard.scan_jacoco.Runner
import org.junit.jupiter.api.Test

class JacocoTest {

    @Test
    internal fun runTest() {
        println("0000000")
        Runner().main(arrayOf("--target-project=/Users/ygdong/git/auto_test_demo"))
    }

}