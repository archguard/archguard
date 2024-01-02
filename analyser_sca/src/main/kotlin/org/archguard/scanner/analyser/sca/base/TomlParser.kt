package org.archguard.scanner.analyser.sca.base

import com.akuleshov7.ktoml.Toml
import com.akuleshov7.ktoml.TomlInputConfig

fun createTomlParser() = Toml(
    inputConfig = TomlInputConfig(
        ignoreUnknownNames = true,
        allowEmptyValues = true,
        allowNullValues = true,
        allowEscapedQuotesInLiteralStrings = true,
        allowEmptyToml = true,
    )
).tomlParser