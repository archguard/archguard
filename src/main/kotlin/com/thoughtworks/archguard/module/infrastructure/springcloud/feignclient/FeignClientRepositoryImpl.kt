package com.thoughtworks.archguard.module.infrastructure.springcloud.feignclient

import com.thoughtworks.archguard.module.domain.springcloud.feignclient.FeignClientRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class FeignClientRepositoryImpl: FeignClientRepository {

    @Autowired
    lateinit var jdbi: Jdbi

    override fun getServiceNameByMethodId(methodId: String): String {
        val sql = "select module from JMethod where id = '$methodId'"
        return jdbi.withHandle<String, Nothing> {
            it.createQuery(sql)
                    .mapTo(String::class.java)
                    .one()
        }
    }
}
