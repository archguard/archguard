# Architecture Analyser

## Run

1. Generate the report:

```bash
java -jar ../scanner_cli/build/libs/scanner_cli-2.2.8-all.jar --language=go --with-structure-cache --type=architecture --output=json --path=. 
```

2. run with JSON output:

```bash
curl -X POST -H "Content-Type: application/json" -d @0_architecture.json http://localhost:3000/api/scanner/1/reporting
```

## Debug run

```bash
cd .. && ./gradlew :analyser_architecture:build
```


```bash
cp build/libs/analyser_architecture-2.2.8-all.jar dependencies/analysers
java -jar ../scanner_cli/build/libs/scanner_cli-2.2.8-all.jar --language=kotlin --type=architecture --output=json --path=/Users/phodal/test/Bilibili-Go-Backup/app 
```


## Empty

```bash
java -jar ../scanner_cli/build/libs/scanner_cli-2.2.8-all.jar --language=java --features=apicalls --features=datamap --output=json --path=/Users/phodal/test/new-protos
```
