package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.repository.CheckStyleRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class CheckStyleRepo : CheckStyleRepository {

    @Autowired
    lateinit var jdbi: Jdbi

    override fun getCheckStyleOverview(): List<String> =
            jdbi.withHandle<List<String>, Nothing> {
                it.createQuery("select message from CheckStyle")
                        .mapTo(String::class.java)
                        .list()
            }


}
