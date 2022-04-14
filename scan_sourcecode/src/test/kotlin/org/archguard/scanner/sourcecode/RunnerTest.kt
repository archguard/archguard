package org.archguard.scanner.sourcecode

import org.junit.jupiter.api.Test
import java.io.File

internal class RunnerTest {

    @Test
    internal fun run_for_function() {
        System.setProperty("dburl", "jdbc:mysql://localhost:3306/")

        val runner = Runner()
        runner.main(
            listOf(
                "--path=.",
                "--language=kotlin",
                "--without-storage",
                "--system-id=2"
            )
        )

        val outputs = listOf(
            "code_annotation.sql",
            "code_annotation_value.sql",
            "code_class.sql",
            "code_field.sql",
            "code_method.sql",
            "code_ref_class_dependencies.sql",
            "code_ref_class_fields.sql",
            "code_ref_class_methods.sql",
            "code_ref_method_callees.sql",
            "container_demand.sql",
            "container_service.sql",
            "data_code_database_relation.sql",
            "database.json",
            "structs.json",
            "apis.json"
        )

        outputs.forEach {
            assert(File(it).exists())
        }
    }
}