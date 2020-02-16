package com.thoughtworks.archguard.git.scanner

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface DBRepository : CrudRepository<CommitHistory, String> {
    @Query("from CommitHistory ch join fetch ch.commits")
    fun get(): CommitHistory
}

