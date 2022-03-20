package com.thoughtworks.archguard.scanner2.infrastructure.mysql

import com.thoughtworks.archguard.scanner2.domain.model.ClassAccess
import com.thoughtworks.archguard.scanner2.domain.model.MethodAccess
import com.thoughtworks.archguard.scanner2.domain.repository.AccessRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class AccessRepositoryImpl(val jdbi: Jdbi) : AccessRepository {
    override fun saveOrUpdateAllClass(systemId: Long, classAccesses: List<ClassAccess>) {
        jdbi.useHandle<Exception> {
            it.createUpdate("delete from code_class_access where system_id=:system_id")
                    .bind("system_id", systemId)
                    .execute()
        }
        classAccesses.forEach {
            save(it, systemId)
        }
    }

    override fun saveOrUpdateAllMethod(systemId: Long, methodAccesses: List<MethodAccess>) {
        jdbi.useHandle<Exception> {
            it.createUpdate("delete from method_access where system_id=:system_id")
                    .bind("system_id", systemId)
                    .execute()
        }
        methodAccesses.forEach {
            save(it, systemId)
        }
    }

    private fun save(clazz: ClassAccess, systemId: Long) {
        jdbi.useHandle<Exception> {
            it.createUpdate("insert into code_class_access (id, class_id, is_abstract, is_interface, system_id) " +
                    "values (:id, :class_id, :is_abstract, :is_interface, :system_id)")
                    .bind("id", UUID.randomUUID().toString())
                    .bind("class_id", clazz.id)
                    .bind("is_abstract", clazz.isAbstract)
                    .bind("is_interface", clazz.isInterface)
                    .bind("system_id", systemId)
                    .execute()
        }
    }

    private fun save(method: MethodAccess, systemId: Long) {
        jdbi.useHandle<Exception> {
            it.createUpdate("insert into method_access (id, method_id, is_abstract, is_synthetic, is_static, is_private, system_id) " +
                    "values (:id, :method_id, :is_abstract, :is_synthetic, :is_static, :is_private, :system_id)")
                    .bind("id", UUID.randomUUID().toString())
                    .bind("method_id", method.id)
                    .bind("is_abstract", method.isAbstract)
                    .bind("is_synthetic", method.isSynthetic)
                    .bind("is_static", method.isStatic)
                    .bind("is_private", method.isPrivate)
                    .bind("system_id", systemId)
                    .execute()
        }
    }
}