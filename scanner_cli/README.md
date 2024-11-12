# ArchGuard Scanner CLI

###

```bash
java -jar scanner_cli-2.2.0-all.jar --help

Options:
  --type [SOURCE_CODE|GIT|DIFF_CHANGES|SCA|RULE|ARCHITECTURE|ESTIMATE|OPENAPI|DOCUMENT]
  --system-id TEXT                 system id
  --server-url TEXT                the base url of the archguard api server
  --workspace TEXT                 the workspace directory
  --path TEXT                      the path of target project
  --output TEXT                    http, csv, json, console
  --output-dir TEXT                output directory
  --analyser-spec TEXT             Override the analysers via json.
  --slot-spec TEXT                 Override the slot via json.
  --language TEXT                  language: Java, Kotlin, TypeScript, CSharp,
                                   Python, Golang.
  --rules TEXT                     rules: webapi, test, sql
  --features TEXT                  features: apicalls, datamap.
  --repo-id TEXT                   repository id used for git analysing
  --branch TEXT                    repository branch
  --started-at INT                 TIMESTAMP, the start date of the scanned
                                   commit
  --since TEXT                     COMMIT ID, the specific revision of the
                                   baseline
  --until TEXT                     COMMIT ID, the specific revision of the
                                   target
  --depth INT                      INTEGER, the max loop depth
  --with-function-code             BOOLEAN, whether to include the function
                                   code
  --debug                          BOOLEAN, whether to enable debug mode
  -h, --help                       Show this message and exit
```

## ArchGuard CLI Usage

```bash
[SCANNER] org.archguard.scanner.ctl.Runner <cli parameters>
|type: GIT
|systemId: 6
|serverUrl: http://localhost:8080
|workspace: /tmp/archguard14370627952499838085
|path: /tmp/archguard14370627952499838085
|output: [http]
<customized analysers>
|analyzerSpec: []
|slotSpec: []
<additional parameters>
|language: Java
|features: []
|repoId: https://gitee.com/thoughtworks/coca
|branch: master
|startedAt: 0
|since: null
|until: null
|depth: 7
|rules: []



[SCANNER] o.a.s.ctl.loader.AnalyserLoader workspace path: /home/spring
[SCANNER] o.a.s.ctl.loader.AnalyserLoader analyser install path: /home/spring/dependencies/analysers
[SCANNER] o.a.s.ctl.loader.AnalyserLoader analyser: git - [2.2.0] is installed
[SCANNER] org.archguard.scanner.ctl.Runner <cli parameters>
|type: SCA
|systemId: 6
|serverUrl: http://localhost:8080
|workspace: /tmp/archguard14370627952499838085
|path: /tmp/archguard14370627952499838085
|output: [http]
<customized analysers>
|analyzerSpec: []
|slotSpec: []
<additional parameters>
|language: java
|features: []
|repoId: null
|branch: master
|startedAt: 0
|since: null
|until: null
|depth: 7
|rules: []


[SCANNER] o.a.s.ctl.loader.AnalyserLoader workspace path: /home/spring
[SCANNER] o.a.s.ctl.loader.AnalyserLoader analyser install path: /home/spring/dependencies/analysers
[SCANNER] o.a.s.ctl.loader.AnalyserLoader analyser: sca - [2.2.0] is installed
[SCANNER] org.archguard.scanner.ctl.Runner <cli parameters>
|type: SOURCE_CODE
|systemId: 6
|serverUrl: http://localhost:8080
|workspace: /tmp/archguard14370627952499838085
|path: /tmp/archguard14370627952499838085
|output: [http]
<customized analysers>
|analyzerSpec: []
|slotSpec: []
<additional parameters>
|language: Java
|features: [apicalls, datamap]
|repoId: null
|branch: master
|startedAt: 0
|since: null
|until: null
|depth: 7
|rules: [webapi, test, sql]
```

## Sample CLI

### push to server

```
java -jar scanner_cli.jar --language=Kotlin --features=apicalls --output=http --output=json --path=. --server-url=http://localhost:8080
```

Or export to json, upload with `cURL`

```bash
curl -X POST -H "Content-Type: application/json" -d @0_apis.json http://localhost:3000/api/scanner/1/reporting
```

### custom slot like test smell

```
java -jar scanner_cli.jar --language=Kotlin --features=apicalls --output=json --path=server --slot-spec='{"identifier": "rule", "host": "https://github.com/archguard/archguard/releases/download/v2.0.0-alpha.17", "version": "2.0.0-alpha.17", "jar": "rule-webapi-2.0.0-alpha.17-all.jar", "className": "org.archguard.linter.rule.webapi.WebApiRuleSlot", "slotType": "rule"}' 
```

### With Arrow Output

```bash
 java  --add-opens=java.base/java.nio=ALL-UNNAMED -jar plugins/scanner-v2.jar --type=source_code --path=. --output=json --language=kotlin --output=arrow
```


