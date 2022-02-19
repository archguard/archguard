package com.thoughtworks.archguard.scanner.infrastructure.db

import com.thoughtworks.archguard.scanner.domain.scanner.git.GitHotFile
import com.thoughtworks.archguard.scanner.domain.scanner.git.GitHotFileRepo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest
@ActiveProfiles("test")
internal class GitHotFileVORepoImplTest(@Autowired val gitHotFileRepo: GitHotFileRepo) {
    
    @Test
    fun shouldUpdateGitHotFilesGivenHistoryGItHotFilesWhenSave() {
        gitHotFileRepo.save(listOf(GitHotFile(1, "repo1", "name1", null, null, 10, null),
                GitHotFile(1, "repo1", "name2", null, null, 10, null)))

        gitHotFileRepo.save(listOf(GitHotFile(1, "repo1", "name1", null, null, 12, null),
                GitHotFile(1, "repo1", "name2", null, null, 13, null)))

        val findBySystemId = gitHotFileRepo.findBySystemId(1);
        
        assertNotNull(findBySystemId)
        assertEquals(2, findBySystemId.size)
        assertEquals("name1", findBySystemId[0].path)
        assertEquals(12, findBySystemId[0].modifiedCount)
        assertEquals(13, findBySystemId[1].modifiedCount)
    }
}