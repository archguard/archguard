package com.thoughtworks.archgard.scanner.infrastructure.db

import com.thoughtworks.archgard.scanner.domain.scanner.git.GitHotFile
import com.thoughtworks.archgard.scanner.domain.scanner.git.GitHotFileVO
import com.thoughtworks.archgard.scanner.domain.scanner.git.GitHotFileRepo
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
        gitHotFileRepo.save(listOf(GitHotFile(GitHotFileVO("name1", 10), 1, "repo1", null), 
                GitHotFile(GitHotFileVO("name2", 10), 1, "repo1", null)))

        gitHotFileRepo.save(listOf(GitHotFile(GitHotFileVO("name1", 12), 1, "repo1", null),
                GitHotFile(GitHotFileVO("name2", 13), 1, "repo1", null)))

        val findBySystemId = gitHotFileRepo.findBySystemId(1);
        
        assertNotNull(findBySystemId)
        assertEquals(2, findBySystemId.size)
        assertEquals("name1", findBySystemId[0].name)
        assertEquals(12, findBySystemId[0].modifiedCount)
        assertEquals(13, findBySystemId[1].modifiedCount)
    }
}