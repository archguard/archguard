package org.archguard.diff.changes

import org.eclipse.jgit.api.Git
import org.jdbi.v3.core.ConnectionException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.io.path.toPath



internal class RunnerTest {
    private val BUILD_DDD = "./build/ddd"

    @BeforeEach
    internal fun setUp() {
        val localPath = File(BUILD_DDD)

        if (!File(localPath, ".git").isDirectory) {
            Git.cloneRepository()
                .setURI("https://github.com/archguard/ddd-monolithic-code-sample")
                .setDirectory(localPath)
                .call()
        }
    }

    @Test
    internal fun should_generate_core_files() {
        System.setProperty("dburl", "jdbc:mysql://localhost:3306/")

        val runner = Runner()
        runner.main(
            listOf(
                "--path=$BUILD_DDD",
                "--since=5952edc",
                "--until=f3fb4e2",
                "--without-storage",
                "--language=java",
                "--system-id=2"
            )
        )

        assert(File("diff-changes.json").exists())
        assert(File("scm_diff_change.sql").exists())

        val text = File("scm_diff_change.sql").readText()
        assert(text.contains("com.dmall.productservice.apis.assembler"))
    }

    @Test
    internal fun crash_for_no_mysql() {
        val thrown: ConnectionException = Assertions.assertThrows(
            ConnectionException::class.java, {
                System.setProperty("dburl", "jdbc:mysql://localhost:3306/")

                val runner = Runner()
                runner.main(
                    listOf(
                        "--path=$BUILD_DDD",
                        "--since=5952edc",
                        "--until=f3fb4e2",
                        "--language=java",
                        "--system-id=2"
                    )
                )
            },
            "Expected doThing() to throw, but it didn't"
        )


        assertTrue(
            (thrown.message!!.contains("Access denied for") || thrown.message!!.contains("Communications link failure"))
        )
    }

    @Test
    internal fun notify_for_since_until() {
        val thrown: NullPointerException = Assertions.assertThrows(
            NullPointerException::class.java, {
                System.setProperty("dburl", "jdbc:mysql://localhost:3306/")

                val runner = Runner()
                runner.main(
                    listOf(
                        "--path=$BUILD_DDD",
                        "--without-storage",
                        "--language=java",
                        "--system-id=2"
                    )
                )
            },
            "Expected doThing() to throw, but it didn't"
        )


        assertTrue(thrown.message!!.contains("git.repository.resolve(sinceRev) must not be null"));
    }
}
