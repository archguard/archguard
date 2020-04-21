package com.thoughtworks.archgard.scanner.domain.project

enum class BuildTool(val target: String) {

    GRADLE("build"),
    MAVEN("target");
}