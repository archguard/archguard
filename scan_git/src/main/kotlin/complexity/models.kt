package com.thoughtworks.archguard.git.scanner.complexity

data class Position(val start: Int, val stop: Int)
data class MethodCognitiveComplexity(val name: String, val param: String, val complexity: Int, val p: Position)