package org.archguard.scanner.ctl

// manual testing for debug
fun main() {
    val args = arrayOf(
        "--type=source_code",
        "--system-id=localtesting",
        "--server-url=http://localhost:8080",
        "--path=/Users/adliao/commit_archguard/archguard_backend",
        "--language=kotlin",
        "--features=apicalls",
        "--features=datamap",
        // "--output=json",
        "--output=http",
        // "--output=console",
    )
    Runner().main(args)
}
