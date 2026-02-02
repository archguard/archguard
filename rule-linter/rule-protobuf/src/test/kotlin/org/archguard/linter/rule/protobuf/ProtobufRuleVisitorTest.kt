package org.archguard.linter.rule.protobuf

import chapi.ast.protobuf.ProtobufAnalyser
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

class ProtobufRuleVisitorTest {
    @Test
    fun `should emit core protobuf naming issues`(@TempDir tempDir: Path) {
        val protoFile = tempDir.resolve("BadFileName.proto").toFile()
        protoFile.writeText(
            """
            syntax = "proto3";
            
            package My.Package;
            
            import "z.proto";
            import "a.proto";
            
            service foo_service {
              rpc get_user (GetUserRequest) returns (GetUserResponse);
            }
            
            message GetUserRequest {
              string userId = 1;
            }
            
            message GetUserResponse {
              string user_id = 1;
            }
            
            message bad_message_name {
              string ok_field = 1;
            }
            """.trimIndent()
        )

        val analyser = ProtobufAnalyser()
        val container = analyser.analysis(protoFile.readText(), protoFile.name)
        val structs = container.DataStructures.map { ds ->
            ds.apply { FilePath = protoFile.absolutePath }
        }

        val issues = ProtobufRuleVisitor(structs).visitor(listOf(ProtobufRuleSetProvider().get()))
        val ruleIds = issues.map { it.ruleId }.toSet()

        assertThat(ruleIds).contains("FILE_NAMES_LOWER_SNAKE_CASE")
        assertThat(ruleIds).contains("PACKAGE_NAME_LOWER_CASE")
        assertThat(ruleIds).contains("IMPORTS_SORTED")
        assertThat(ruleIds).contains("MESSAGE_NAMES_UPPER_CAMEL_CASE")
        assertThat(ruleIds).contains("FIELD_NAMES_LOWER_SNAKE_CASE")
        assertThat(ruleIds).contains("SERVICE_NAMES_UPPER_CAMEL_CASE")
        assertThat(ruleIds).contains("RPC_NAMES_UPPER_CAMEL_CASE")
    }
}

