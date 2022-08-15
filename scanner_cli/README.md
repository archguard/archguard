## scanner cli

全权(规划中)接收archguard的扫描请求, 并将结果发送到archguard的服务器.

- 判断执行条件
- 拉取scanner lib, 包括自定义的
- 执行计算并发送结果

### 后续

接收HTTP的扫描请求, 以实现分布式运行
拆分和规整已有的scanner

## Sample CLI

### push to server

```
java -jar scanner_cli.jar --language=Kotlin --features=apicalls --output=http --output=json --path=. --server-url=http://localhost:8080
```

### custom slot like test smell

```
java -jar scanner_cli.jar --language=Kotlin --features=apicalls --output=json --path=server --slot-spec='{"identifier": "rule", "host": "https://github.com/archguard/archguard/releases/download/v2.0.0-alpha.17", "version": "2.0.0-alpha.17", "jar": "rule-webapi-2.0.0-alpha.17-all.jar", "className": "org.archguard.linter.rule.webapi.WebApiRuleSlot", "slotType": "rule"}' 
```
