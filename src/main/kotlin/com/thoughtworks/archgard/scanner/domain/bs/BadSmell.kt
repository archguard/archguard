package com.thoughtworks.archgard.scanner.domain.bs

data class BadSmell(val entityName: String,
                    val line: Int,
                    val description: String,
                    val size: Int,
                    val type: String)
