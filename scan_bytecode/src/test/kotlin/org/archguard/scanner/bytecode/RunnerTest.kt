package org.archguard.scanner.bytecode

import org.jdbi.v3.core.ConnectionException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.io.path.toPath

internal class RunnerTest {
    @Test
    internal fun should_generate_core_files() {
        System.setProperty("dburl", "jdbc:mysql://localhost:3306/")

        val resource = this.javaClass.classLoader.getResource("e2e").toURI().toPath().toFile().absolutePath

        val runner = Runner()
        runner.main(
            listOf(
                "--path=$resource",
                "--language=jvm",
                "--without-storage",
                "--system-id=2"
            )
        )

        assert(File("code_annotation.sql").exists())
        assert(File("code_annotation_value.sql").exists())
        assert(File("code_class.sql").exists())
        assert(File("code_field.sql").exists())
        assert(File("code_method.sql").exists())
        assert(File("code_ref_class_dependencies.sql").exists())
        assert(File("code_ref_class_fields.sql").exists())
        assert(File("code_ref_class_methods.sql").exists())
        assert(File("code_ref_method_callees.sql").exists())
        assert(File("container_service.sql").exists())
        assert(File("container_resource.sql").exists())
        assert(File("apis.json").exists())

        val values = File("code_annotation_value.sql").readText()
        assert(values.contains("/api/systems/{systemId}/code-tree"))

        val resources = File("container_resource.sql").readText()
        assert(resources.contains("/api/systems/{systemId}/code-tree"))
    }

    @Test
    internal fun crash_for_no_mysql() {
        val thrown: ConnectionException = Assertions.assertThrows(
            ConnectionException::class.java, {
                System.setProperty("dburl", "jdbc:mysql://localhost:3306/")

                val runner = Runner()
                runner.main(
                    listOf(
                        "--path=.",
                        "--language=jvm",
                        "--system-id=2"
                    )
                )
            },
            "Expected doThing() to throw, but it didn't"
        )


        Assertions.assertTrue(
            (thrown.message!!.contains("Access denied for") || thrown.message!!.contains("Communications link failure"))
        )
    }
}