package com.thoughtworks.archguard.git.scanner

/* core scanner
*
*
* */

class ScannerService(private val gitAdapter: GitAdapter, private val dbRepository: DBRepository) {
    fun scan(config: Config) {
        val gitRepository = gitAdapter.scan(config)
        dbRepository.saveGitRepository(gitRepository)
    }
}