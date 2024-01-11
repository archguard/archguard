package org.archguard.architecture.view.code

import kotlinx.serialization.Serializable

@Serializable
class CodeArchitecture(val language: Language, val type: RepositoryType)