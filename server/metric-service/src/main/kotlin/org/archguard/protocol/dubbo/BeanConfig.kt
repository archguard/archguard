package org.archguard.protocol.dubbo

import org.archguard.protocol.dubbo.SubModuleDubbo

data class BeanConfig(val id: String, val implClass: String, val subModule: SubModuleDubbo)
