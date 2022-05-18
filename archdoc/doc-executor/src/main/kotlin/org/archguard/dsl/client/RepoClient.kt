package org.archguard.dsl.client

import org.archguard.dsl.model.Repository

interface RepoClient {
    fun save(repo: Repository)
}