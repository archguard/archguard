package com.thoughtworks.archguard.git.scanner

import org.springframework.data.repository.CrudRepository

interface DBRepository : CrudRepository<CommitHistory, String>

