package com.thoughtworks.archgard.scanner.domain.project

enum class BuildTool(val target: String) {

    NONE("none"),
    GRADLE("build"),
    MAVEN("target");
}
