package org.archguard.scanner.ctl

// manual testing for debug
fun main() {
    ManualTest.scan()
}

internal object ManualTest {
    fun scan() {
        val args = sca(
            arrayOf(
                "--system-id=2",
                "--server-url=http://localhost:8080",
                "--output=json",
                "--output=http",
                // "--output=console",
            )
        )
        Runner().main(args)
    }

    private fun sourcecode(array: Array<String>) = array + arrayOf(
        "--path=/var/folders/w8/3_cj5bnx7hqcw5ghz51dn_9h0000gn/T/archguard4201047547240422815/archguard", // frontend
        "--type=source_code",
        "--language=typescript",
        // "--features=apicalls",
        // "--features=datamap",
    )

    private fun diffchanges(array: Array<String>) = array + arrayOf(
        "--path=/var/folders/w8/3_cj5bnx7hqcw5ghz51dn_9h0000gn/T/archguard6517240585951136059", // backend
        "--type=diff_changes",
        "--branch=master",
        "--until=37b2591d911d74c35f2a80b56c863a0faff19f82",
        "--since=5ba7afff009119f6d198f37f5f5803ed8e7f6ff8",
    )

    private fun sca(array: Array<String>) = array + arrayOf(
        "--path=/var/folders/w8/3_cj5bnx7hqcw5ghz51dn_9h0000gn/T/archguard6517240585951136059", // backend
        "--type=sca",
        "--language=kotlin",
    )

    private fun git(array: Array<String>) = array + arrayOf(
        "--path=/var/folders/w8/3_cj5bnx7hqcw5ghz51dn_9h0000gn/T/archguard4201047547240422815/archguard", // frontend
        "--type=git",
        "--repo-id=frontend",
        "--branch=master",
        "--started-at=0",
    )
}
