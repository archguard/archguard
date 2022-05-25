package org.archguard.dsl.base.client

import org.archguard.dsl.base.model.Repository

interface RepoClient {
    fun saveRepos(repo: Iterable<Repository>)
    fun save(repo: Repository)
}