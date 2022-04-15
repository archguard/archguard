package org.archguard.scanner.bytecode

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

internal class RunnerTest {

    @BeforeEach
    internal fun setUp() {
        System.setProperty("dburl", "jdbc:mysql://localhost:3306/")

        val runner = Runner()
        runner.main(
            listOf(
                "--path=src/test/resources/e2e",
                "--language=jvm",
                "--without-storage",
                "--system-id=2"
            )
        )
    }

    @Test
    internal fun should_generate_core_files() {
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
    }

    @Test
    internal fun should_contain_annotation_value() {
        val values = File("code_annotation_value.sql").readText()
        assert(values.contains("/api/systems/{systemId}/code-tree"))
    }

    @Test
    internal fun should_contain_api_resource() {
        val values = File("container_resource.sql").readText()
        assert(values.contains("/api/systems/{systemId}/code-tree"))
    }
}