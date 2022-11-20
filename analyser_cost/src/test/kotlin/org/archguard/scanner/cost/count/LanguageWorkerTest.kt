package org.archguard.scanner.cost.count

import io.kotest.matchers.shouldBe
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

        worker.countStats(job)!!

        job.lines shouldBe 14
        job.code shouldBe 11
        job.comment shouldBe 2
        job.blank shouldBe 1
    }

    @Test
    fun checkComplexity() {
        val content = """for (int i=0; i<100; i++) {""".toByteArray()
        val job = FileJob(
            language = "Java",
            content = content,
            bytes = content.size.toLong(),
        )
        worker.countStats(job)!!

        job.complexity shouldBe 1
    }

    @Test
    fun checkComplexity2() {
        val content = """void sort(int * A) {
   int i=0;
   int n=4;
   int j = 0;
   while (i < n-1)
   {
       j = i +1
       while (j < n)
       {
           if (A[i] < A[j]) swap(A[i], A[j]);
       }
       i = i + 1
   }
}
""".toByteArray()
        val job = FileJob(
            language = "C",
            content = content,
            bytes = content.size.toLong(),
        )
        worker.countStats(job)!!

        job.complexity shouldBe 3
    }
}
