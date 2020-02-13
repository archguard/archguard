import com.thoughtworks.archguard.git.scanner.Config
import com.thoughtworks.archguard.git.scanner.JGitAdapter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class JGitAdapterTest {

    @Test
    fun scan() {
        //todo : 怎么省略后两个参数？
        val gitRepository = JGitAdapter().scan(Config(path = "/Users/ygdong/Downloads/gittest", branch = null, lastCommit = null))
        val branch = gitRepository.branch
        assertEquals("master", branch)
        println("gitRepository = ${gitRepository}")
    }
}