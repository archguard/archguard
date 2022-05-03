package org.archguard.scanner.ctl

// manual testing for debug
fun main() {
    val args = arrayOf(
        "--type=source_code",
        "--system-id=3",
        "--server-url=http://localhost:8080",
        "--path=/var/folders/w8/3_cj5bnx7hqcw5ghz51dn_9h0000gn/T/archguard11450798564056611470",
        "--language=kotlin",
        "--features=apicalls",
        // "--features=datamap",
        "--output=json",
        "--output=http",
        // "--output=console",
    )
    Runner().main(args)
}
