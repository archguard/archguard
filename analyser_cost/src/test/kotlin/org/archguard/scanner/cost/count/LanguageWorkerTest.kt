package org.archguard.scanner.cost.count

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test

class LanguageWorkerTest {

    // setup
    private val worker = LanguageWorker()

    @Test
    fun bomSkip() {
        val content = byteArrayOf(239.toByte(), 187.toByte(), 191.toByte())
        val result = LanguageWorker.checkBomSkip(FileJob(content = content))
        assert(result == content.size)
    }

    @Test
    fun bomSkip2() {
        val content = """   // Comment 1
namespace Baz
{
    using System;

    public class FooClass
    {
        public void Test(string report)
        {
          // Comment 2
          throw new NotImplementedException();
        }
    }
}""".toByteArray()
        val job = FileJob(
            language = "C#",
            content = content,
            bytes = content.size.toLong(),
        )

        val countStates = worker.countStates(job)!!
        println(Json.encodeToString(countStates))
    }
}
