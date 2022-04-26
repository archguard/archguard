package org.archguard.linter.rule.testcode.rules

import chapi.domain.core.CodeCall
import chapi.domain.core.CodeFunction
import org.archguard.rule.core.Severity
import org.archguard.rule.core.IssueEmit
import org.archguard.linter.rule.testcode.TbsRule
import org.archguard.linter.rule.testcode.smellPosition

class SleepyTestRule : TbsRule() {
    init {
        this.id = "sleep-test"
        this.name = "SleepyTest"
        this.key = this.javaClass.name
        this.description = "测试用例中包含 Sleep 休眠语句，常见于异步测试场景，或为了规避不同测试用例中某些操作的依赖和冲突。"
        this.message = "如 Robert C. Martin 在《代码整洁之道》所说的那样，好的测试应该是快速（Fast）、独立（Indendent）、可重复（Repeatable）、自足验证（Self-Validating）、及时（Timely）的\n" +
                "  · 测试用例本身编写时，应该注意保持独立性，不同用例之前不要产生互相依赖、等待，尤其是不同用例使用的测试数据应该独立。\n" +
                "  · 当处理异步测试场景时，建议使用 CompletableFuture 配合 CountDownLatch 的方式进行，从而不用硬编码休眠的时长。"
        this.severity = Severity.WARN
    }

    override fun visitFunctionCall(function: CodeFunction, codeCall: CodeCall, index: Int, callback: IssueEmit) {
        if (codeCall.FunctionName == "sleep" && codeCall.NodeName == "Thread") {
            callback(this, codeCall.Position.smellPosition())
        }
    }
}