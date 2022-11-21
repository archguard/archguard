package org.archguard.scanner.cost.count

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.io.File

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

    @Test
    fun processByFileFromResource() {
        val path = this.javaClass.classLoader.getResource("c/demo.c")!!.file
        val fileJob = worker.processFile(File(path))!!

        fileJob.language shouldBe "C"
        fileJob.possibleLanguages shouldBe listOf("C")
        fileJob.filename shouldBe "demo.c"
        fileJob.extension shouldBe "c"
//        fileJob.location shouldBe path
        fileJob.complexity shouldBe 2
        fileJob.lines shouldBe 12
        fileJob.code shouldBe 9
        fileJob.blank shouldBe 3
        fileJob.content.size shouldBe 169
    }

    @Test
    fun processKotlinDemo() {
        val content = """// based on [https://github.com/boyter/scc](https://github.com/boyter/scc) with MIT LICENSE.
// SPDX-License-Identifier: MIT OR Unlicense
// languages.json based on [https://github.com/boyter/scc](https://github.com/boyter/scc) with MIT LICENSE.

package org.archguard.scanner.cost.count

class LanguageServices {
    /**
     * DetermineLanguage given a filename, fallback language, possible languages and content make a guess to the type.
     * If multiple possible it will guess based on keywords similar to how https://github.com/vmchale/polyglot does
     */
     fun determineLanguage(fallbackLanguage: String, possibleLanguages: List<String>, content: ByteArray): String {

     }
}

""".toByteArray()

        val job = FileJob(
            language = "Kotlin",
            content = content,
            bytes = content.size.toLong(),
        )

        worker.countStats(job)!!

        job.lines shouldBe 16
        job.code shouldBe 2
    }
}
