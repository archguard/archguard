package com.thoughtworks.archguard.workbench.infrastructure

import com.thoughtworks.archguard.workbench.domain.AacDslCodeModel
import com.thoughtworks.archguard.workbench.domain.AasDslRepository
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.mapTo
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class AasDslRepositoryImpl : AasDslRepository {
    @Autowired
    lateinit var jdbi: Jdbi

    @GetGeneratedKeys
    override fun save(model: AacDslCodeModel): Long {
        return jdbi.withHandle<Long, Nothing> {
            it.createUpdate(
                "insert into aac_dsl" +
                        "(id, description, content, title, author) " +
                        "values (:id, :description, :content, :title, :author)"
            )
                .bindBean(model)
                .executeAndReturnGeneratedKeys("id")
                .mapTo(Long::class.java)
                .one()
        }
    }

    override fun update(model: AacDslCodeModel): Int {
        return jdbi.withHandle<Int, Nothing> {
            it.createUpdate(
                "update aac_dsl set content = :content where id = :id"
            )
                .bindBean(model)
                .execute()
        }
    }

    override fun getById(id: Long): AacDslCodeModel? =
        jdbi.withHandle<AacDslCodeModel, Nothing> {
            it.createQuery(
                "select id, content, description, title from aac_dsl where id = :id"
            )
                .bind("id", id)
                .mapTo<AacDslCodeModel>()
                .firstOrNull()
        }

}