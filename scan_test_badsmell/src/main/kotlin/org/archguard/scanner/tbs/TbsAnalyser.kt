package org.archguard.scanner.tbs

import chapi.app.analyser.ChapiAnalyser
import chapi.app.analyser.config.ChapiConfig
import chapi.domain.core.CodeAnnotation
import chapi.domain.core.CodeCall
import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeFunction
import kotlinx.serialization.Serializable

@Serializable
data class TestBadSmell(
    var FileName: String = "",
    var Type: String = "",
    var Description: String = "",
    var Line: Int = 0
)

data class TbsResult(var results: Array<TestBadSmell>)

class TbsAnalyser(
    private val config: ChapiConfig = ChapiConfig()
) {
    fun analysisByPath(path: String): Array<TestBadSmell> {
        val nodes = ChapiAnalyser(config).analysis(path)
        val tbsResult = TbsResult(arrayOf())
        val callMethodMap = buildCallMethodMap(nodes)

        for (node in nodes) {
            for (method in node.Functions) {
                if (!method.isJUnitTest()) {
                    continue
                }

                val currentMethodCalls = addExtractAssertMethodCall(method, node, callMethodMap)
                method.FunctionCalls = currentMethodCalls

                for (annotation in method.Annotations) {
                    checkIgnoreTest(node.FilePath, annotation, tbsResult, method)
                    checkEmptyTest(node.FilePath, annotation, tbsResult, method)
                }

                val methodCallMap = hashMapOf<String, Array<CodeCall>>()
                var hasAssert = false

                for ((index, funcCall) in currentMethodCalls.withIndex()) {
                    if (funcCall.FunctionName == "") {
                        val lastFuncCall = index == currentMethodCalls.size - 1
                        if (lastFuncCall && !hasAssert) {
                            appendUnknownTest(node.FilePath, method, tbsResult)
                        }
                        continue
                    }

                    updateMethodCallMap(funcCall, methodCallMap)

                    checkRedundantPrintTest(node.FilePath, funcCall, tbsResult)
                    checkSleepyTest(node.FilePath, funcCall, tbsResult)
                    checkRedundantAssertionTest(node.FilePath, funcCall, tbsResult)

                    if (funcCall.hasAssertion()) hasAssert = true

                    val lastFuncCall = index == currentMethodCalls.size - 1
                    if (lastFuncCall && !hasAssert) {
                        appendUnknownTest(node.FilePath, method, tbsResult)
                    }
                }

                checkDuplicateAssertTest(node, method, methodCallMap, tbsResult)
            }
        }

        return tbsResult.results
    }

    private fun addExtractAssertMethodCall(
        method: CodeFunction,
        node: CodeDataStruct,
        callMethodMap: MutableMap<String, CodeFunction>
    ): Array<CodeCall> {
        var methodCalls = method.FunctionCalls
        for (methodCall in methodCalls) {
            if (methodCall.NodeName == node.NodeName) {
                val mapFunc = callMethodMap[methodCall.buildFullMethodName()]
                if (mapFunc != null && mapFunc.Name != "") {
                    methodCalls += mapFunc.FunctionCalls
                }
            }
        }

        return methodCalls
    }

    private fun updateMethodCallMap(
        funcCall: CodeCall,
        methodCallMap: HashMap<String, Array<CodeCall>>
    ) {
        var calls: Array<CodeCall> = arrayOf()
        val buildFullMethodName = funcCall.buildFullMethodName()
        if (methodCallMap[buildFullMethodName] != null) {
            calls = methodCallMap[buildFullMethodName]!!
        }
        calls += funcCall
        methodCallMap[buildFullMethodName] = calls
    }

    private fun checkDuplicateAssertTest(
        node: CodeDataStruct,
        method: CodeFunction,
        methodCallMap: MutableMap<String, Array<CodeCall>>,
        tbsResult: TbsResult
    ) {
        var isDuplicateTest = false
        for (entry in methodCallMap) {
            val methodCalls = entry.value
            val duplicatedLimitLength = 5
            if (methodCalls.size >= duplicatedLimitLength) {
                if (methodCalls.last().hasAssertion()) {
                    isDuplicateTest = true
                }
            }
        }

        if (isDuplicateTest) {
            val testBadSmell = TestBadSmell(
                FileName = node.FilePath,
                Type = "DuplicateAssertTest",
                Description = "",
                Line = method.Position.StartLine
            )

            tbsResult.results += testBadSmell
        }
    }

    private fun appendUnknownTest(filePath: String, method: CodeFunction, tbsResult: TbsResult) {
        val testBadSmell = TestBadSmell(
            FileName = filePath,
            Type = "UnknownTest",
            Description = "",
            Line = method.Position.StartLine
        )

        tbsResult.results += testBadSmell
    }

    private fun checkRedundantAssertionTest(
        filePath: String,
        funcCall: CodeCall,
        tbsResult: TbsResult
    ) {
        val assertParametersSize = 2
        if (funcCall.Parameters.size == assertParametersSize) {
            if (funcCall.Parameters[0].TypeValue == funcCall.Parameters[1].TypeValue) {
                val testBadSmell = TestBadSmell(
                    FileName = filePath,
                    Type = "RedundantAssertionTest",
                    Description = "",
                    Line = funcCall.Position.StartLine
                )

                tbsResult.results += testBadSmell
            }
        }
    }

    private fun checkSleepyTest(filePath: String, funcCall: CodeCall, tbsResult: TbsResult) {
        if (funcCall.isThreadSleep()) {
            val testBadSmell = TestBadSmell(
                FileName = filePath,
                Type = "SleepyTest",
                Description = "",
                Line = funcCall.Position.StartLine
            )

            tbsResult.results += testBadSmell
        }
    }

    private fun checkRedundantPrintTest(filePath: String, funcCall: CodeCall, tbsResult: TbsResult) {
        if (funcCall.isSystemOutput()) {
            val testBadSmell = TestBadSmell(
                FileName = filePath,
                Type = "RedundantPrintTest",
                Description = "",
                Line = funcCall.Position.StartLine
            )

            tbsResult.results += testBadSmell
        }
    }

    private fun checkIgnoreTest(
        filePath: String,
        annotation: CodeAnnotation,
        tbsResult: TbsResult,
        method: CodeFunction
    ) {
        if (annotation.isIgnore()) {
            val testBadSmell = TestBadSmell(
                FileName = filePath,
                Type = "IgnoreTest",
                Description = "",
                Line = method.Position.StartLine
            )

            tbsResult.results += testBadSmell
        }
    }

    private fun checkEmptyTest(
        filePath: String,
        annotation: CodeAnnotation,
        tbsResult: TbsResult,
        method: CodeFunction
    ) {
        val isJavaTest = filePath.endsWith(".java") && annotation.isTest()
        val isGoTest = filePath.endsWith("_test.go")
        if (isJavaTest || isGoTest) {
            if (method.FunctionCalls.size <= 1) {
                val badSmell = TestBadSmell(
                    FileName = filePath,
                    Type = "EmptyTest",
                    Description = "",
                    Line = method.Position.StartLine
                )

                tbsResult.results += badSmell
            }
        }
    }

    private fun buildCallMethodMap(nodes: Array<CodeDataStruct>): MutableMap<String, CodeFunction> {
        val callMethodMap: MutableMap<String, CodeFunction> = mutableMapOf()
        for (node in nodes) {
            for (method in node.Functions) {
                callMethodMap[method.buildFullMethodName(node)] = method
            }
        }

        return callMethodMap
    }
}
