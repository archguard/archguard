package com.thoughtworks.archguard.report.domain.cohesion;

import com.thoughtworks.archguard.report.domain.ValidPagingParam
import com.thoughtworks.archguard.report.domain.cohesion.ShotgunSurgeryRepository
import org.archguard.smell.BadSmellType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import kotlin.io.path.Path

class ShotgunSurgeryServiceTest {

    private val shotgunSurgeryRepository = mock(ShotgunSurgeryRepository::class.java)
    private val shotgunSurgeryService = ShotgunSurgeryService(shotgunSurgeryRepository)

    @Test
    fun `should return shotgun surgery with total count`() {
        // given
        val systemId = 1L
        val limit = 10L
        val offset = 0L
        val commitIds = listOf("commit1", "commit2")
        val mockPaths = listOf(
            Path("src/main/java/com/example/ClassA.java"),
            Path("src/main/java/com/example/ClassB.java"),
            Path("src/main/java/com/example/ClassC.java")
        )

        val mockShotgunSurgery = ShotgunSurgery(
            commitId = "123456",
            commitMessage = "Fix shotgun surgery issue",
            paths = mockPaths
        )

        val shotgunSurgeryList = listOf(
            mockShotgunSurgery,
            mockShotgunSurgery
        )

        `when`(shotgunSurgeryRepository.getShotgunSurgeryCommitIds(systemId, ShotgunSurgery.LIMIT)).thenReturn(commitIds)
        `when`(shotgunSurgeryRepository.getShotgunSurgery(commitIds, limit, offset)).thenReturn(shotgunSurgeryList)

        // when
        val result = shotgunSurgeryService.getShotgunSurgeryWithTotalCount(systemId, limit, offset)

        // then
        assertEquals(commitIds.size.toLong(), result.first)
        assertEquals(shotgunSurgeryList, result.second)
    }

    @Test
    fun `should return shotgun surgery count`() {
        // given
        val systemId = 1L
        val commitIds = listOf("commit1", "commit2")

        `when`(shotgunSurgeryRepository.getShotgunSurgeryCommitIds(systemId, ShotgunSurgery.LIMIT)).thenReturn(commitIds)

        // when
        val result = shotgunSurgeryService.getShotgunSurgeryCount(systemId)

        // then
        assertEquals(commitIds.size.toLong(), result)
    }

    @Test
    fun `should return cohesion report`() {
        // given
        val systemId = 1L
        val commitIds = listOf("commit1", "commit2")

        `when`(shotgunSurgeryRepository.getShotgunSurgeryCommitIds(systemId, ShotgunSurgery.LIMIT)).thenReturn(commitIds)

        // when
        val result = shotgunSurgeryService.getCohesionReport(systemId)

        // then
        assertEquals(mapOf(BadSmellType.SHOTGUN_SURGERY to commitIds.size.toLong()), result)
    }
}
