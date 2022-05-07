package org.archguard.scanner.ctl

// manual testing for debug
fun main() {
    ManualTest.scan()
}

internal object ManualTest {
    fun scan() {
        val args = arrayOf(
            "--type=source_code",
            "--system-id=2",
            "--server-url=http://localhost:8080",
            "--path=/var/folders/w8/3_cj5bnx7hqcw5ghz51dn_9h0000gn/T/archguard11975424402559343478/archguard",
            "--language=typescript",
            // "--features=apicalls",
            // "--features=datamap",
            "--output=json",
            // "--output=http",
            // "--output=console",
        )
        Runner().main(args)
    }
}
