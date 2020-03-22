package com.thoughtworks.archgard.hub.domain.helper

import com.thoughtworks.archgard.scanner.domain.ScanContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class ContextGenerator {

    @Value("\${workspace}")
    private lateinit var workspace: String


    @Bean
    fun getContext(): ScanContext {
        val context = ScanContext()
        context.workspace = workspace + "/" + Date().time
        return context
    }

}