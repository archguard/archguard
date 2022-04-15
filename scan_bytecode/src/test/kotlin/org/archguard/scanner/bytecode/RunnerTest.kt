package org.archguard.scanner.bytecode

import org.junit.jupiter.api.Test
import java.io.File

internal class RunnerTest {
    @Test
    internal fun run_main() {
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
        assert(File("apis.json").exists())
    }
}