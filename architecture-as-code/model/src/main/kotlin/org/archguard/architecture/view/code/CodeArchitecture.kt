package org.archguard.architecture.view.code

import kotlinx.serialization.Serializable

@Serializable
class CodeArchitecture(val type: RepositoryType, val stacks: List<String> = listOf())