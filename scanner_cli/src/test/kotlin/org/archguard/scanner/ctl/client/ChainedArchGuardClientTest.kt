package org.archguard.scanner.ctl.client

import io.mockk.every
import io.mockk.mockkConstructor
import java.io.File
import kotlin.test.Test

class ChainedArchGuardClientTest {
    @Test
    fun shouldCovertAllLoalClients() {
        // given
        val systemId = "systemId"
//        mockkConstructor(File::class)
//        every { anyConstructed<File>().writeText(any()) } returns Unit

        val clients = listOf(
            ArchGuardCsvClient(systemId),
            ArchGuardJsonClient(systemId),
            ArchGuardConsoleClient(systemId),
        )

        // when
        val chainedArchGuardClient = ChainedArchGuardClient(clients)
        chainedArchGuardClient.saveDataStructure(emptyList())
        chainedArchGuardClient.saveApi(emptyList())
        chainedArchGuardClient.saveRelation(emptyList())
        chainedArchGuardClient.saveGitLogs(emptyList())
        chainedArchGuardClient.saveDiffs(emptyList())
        chainedArchGuardClient.saveDependencies(emptyList())
        chainedArchGuardClient.saveRuleIssues(emptyList())
        chainedArchGuardClient.saveEstimates(emptyList())
        chainedArchGuardClient.saveOpenApi(emptyList())

        // then
    }
}