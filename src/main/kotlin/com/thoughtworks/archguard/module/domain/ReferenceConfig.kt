package com.thoughtworks.archguard.module.domain

data class ReferenceConfig(val id: String, val beanId: String, val interfaceName: String, val version: String?,
                           val group: String?, val subModule: SubModule)