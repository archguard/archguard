package org.archguard.codedb.code

import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface CodeRepository : ReactiveMongoRepository<CodeDocument, String>