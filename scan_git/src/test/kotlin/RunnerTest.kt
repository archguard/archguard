import com.thoughtworks.archguard.git.scanner.main
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File

internal class RunnerTest {

    @Test
    fun run() {
        val file = File("output.sql")
        file.deleteOnExit();
        main(arrayOf("--git-path=../code-scanners", "--branch=master"))
        assertTrue(file.exists())
    }
}