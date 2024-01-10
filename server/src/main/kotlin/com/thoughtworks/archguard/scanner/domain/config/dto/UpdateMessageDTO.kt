package com.thoughtworks.archguard.scanner.domain.config.dto

import kotlinx.serialization.Serializable

data class UpdateMessageDTO(var success: Boolean, var message: String)

@Serializable
data class UpdateDTO(var id: String, var value: String)
