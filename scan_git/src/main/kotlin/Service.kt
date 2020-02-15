package com.thoughtworks.archguard.git.scanner

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/*
* core scanner
*/

@Service
class ScannerService(
        @Autowired private val gitAdapter: GitAdapter,
        @Autowired private val dbRepository: DBRepository) {
    fun scan(config: Config) {
        val commitHistory = gitAdapter.scan(config)
        dbRepository.save(commitHistory)
    }

    fun findAll(): MutableIterable<CommitHistory> = dbRepository.findAll()

}