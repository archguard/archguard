package org.archguard.scanner.gradle.plugin

import groovy.lang.Closure
import org.gradle.api.Project
import javax.inject.Inject

const val DEFAULT_OUTPUT_FILE = "template-example.txt"

@Suppress("UnnecessaryAbstractClass")
abstract class ArchguardExtension @Inject constructor(project: Project) {
    /**
     * The server url of Archguard backend, default to [http://localhost:8088]
     */
    var serverUrl: String = "http://localhost:8080"

    /**
     * The supported languages: [java, kotlin, javascript, typescript, python, go ...], default to [java]
     */
    var language: String = "java"

    /**
     * The supported features: ["apicalls", ""]
     */
    var features: List<String> = listOf()

    /**
     * The Archguard Slots configuration for the project
     */
    val slots = project.container(SlotConfiguration::class.java) {
        SlotConfiguration(project)
    }

    fun slots(config: SlotConfigContainer.() -> Unit) {
        slots.configure(object : Closure<Unit>(this, this) {
            fun doCall() {
                @Suppress("UNCHECKED_CAST")
                (delegate as? SlotConfigContainer)?.let {
                    config(it)
                }
            }
        })
    }

    fun slots(config: Closure<Unit>) {
        slots.configure(config)
    }
}
