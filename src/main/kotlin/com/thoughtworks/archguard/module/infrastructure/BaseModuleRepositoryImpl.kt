package com.thoughtworks.archguard.module.infrastructure

import com.thoughtworks.archguard.module.domain.BaseModuleRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class BaseModuleRepositoryImpl : BaseModuleRepository {

    @Autowired
    lateinit var jdbi: Jdbi

    override fun getBaseModules(): List<String> {
        return jdbi.withHandle<List<String>, Nothing> {
            it.createQuery("select distinct module from JClass")
                    .mapTo(String::class.java)
                    .list()
                    .filter { it -> it != "null" }
        }
    }

}
