package com.thoughtworks.archguard.scanner.domain.system

enum class BuildTool(val target: String) {

    NONE("none"),
    GRADLE("build"),
    MAVEN("target");
}
