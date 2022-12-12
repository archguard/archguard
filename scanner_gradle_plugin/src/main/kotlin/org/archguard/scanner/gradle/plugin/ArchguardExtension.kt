package org.archguard.scanner.gradle.plugin

import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
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

    private val objects = project.objects

    // Example of a property that is optional.
    val tag: Property<String> = objects.property(String::class.java)

    // Example of a property with a default set with .convention
    val outputFile: RegularFileProperty = objects.fileProperty().convention(
        project.layout.buildDirectory.file(DEFAULT_OUTPUT_FILE)
    )
}
