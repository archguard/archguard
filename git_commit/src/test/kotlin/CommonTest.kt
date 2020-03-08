import com.thoughtworks.archguard.git.analyzer.Runner
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.spring4.JdbiFactoryBean
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [Runner::class])
class CommonTest {

    @Autowired
    private lateinit var factory: JdbiFactoryBean

    private var jdbi: Jdbi? = null


    @BeforeEach
    internal fun setUp() {
        jdbi = factory.`object`
    }

    @Test
    internal fun testJupiterConfig() {
        assertTrue(true)

        val result = jdbi!!.withHandle<Int, Exception> { handle ->
            handle.execute("create table mytest(id int)")
            handle.execute("insert into mytest(id) values(?) ", 100)

            handle.createQuery("select * from mytest")
                    .mapTo(Int::class.java)
                    .one()
        }

        assertEquals(100, result)
    }


}