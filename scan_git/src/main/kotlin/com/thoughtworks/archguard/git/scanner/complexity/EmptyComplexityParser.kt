package com.thoughtworks.archguard.git.scanner.complexity

class EmptyComplexityParser : ICognitiveComplexityParser {
    override fun processFile(file: String): List<MethodCognitiveComplexity> {
        return listOf()
    }

    override fun processCode(code: String): List<MethodCognitiveComplexity> {
        return listOf()
    }
}