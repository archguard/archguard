package org.archguard.dsl.client

import org.archguard.dsl.model.Repository

interface RepoClient {
    fun saveRepos(repo: Iterable<Repository>)
    fun save(repo: Repository)
}