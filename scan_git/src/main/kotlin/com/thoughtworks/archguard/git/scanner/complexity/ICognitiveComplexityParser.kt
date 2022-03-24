package com.thoughtworks.archguard.git.scanner.complexity


interface ICognitiveComplexityParser {
    fun processFile(file: String): List<MethodCognitiveComplexity>;
    fun processCode(code: String): List<MethodCognitiveComplexity>;
}