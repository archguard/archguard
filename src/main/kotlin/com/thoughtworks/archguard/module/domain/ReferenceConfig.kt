package com.thoughtworks.archguard.module.domain

data class ReferenceConfig(val beanId: String, val interfaceName: String, val version: String?,
                           val group: String?, val subModule: SubModule)