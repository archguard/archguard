# Architecture Analyser

Run:

```bash
java -jar scanner_cli.jar --language=go --type=architecture --output=json --path=server 
```

Debug run

```bash
cd .. && ./gradlew :analyser_architecture:build
```


```bash
cp build/libs/analyser_architecture-2.1.5-all.jar dependencies/analysers
java -jar ../scanner_cli/build/libs/scanner_cli-2.1.5-all.jar --language=kotlin --type=architecture --output=json --path=/Users/phodal/test/Bilibili-Go-Backup/app 
```
