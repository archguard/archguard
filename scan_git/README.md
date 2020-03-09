### 说明：
程序会扫描指定的 GIT 仓库， 并在命令当前目录下生成SQL文件 output.sql, 可以通过一下命令倒入到DB ：
` 
mysql --force --verbose --host=localhost --port=3306 --database=archguard --user=root --password= -e "source /Users/ygdong/git/code-scanners/scan_git/output.sql"
`
应用启动时，会调用 flyway 变更DB，DB Schema 版本脚本 在 src/resources/db/migration 目录下

### 构建：
-  `mvn clean package spring-boot:repackage -DskipTests`

### 运行：

`java -jar target/scan_git-*.jar --gitPath=/somepath/gittest/ --branch=master`

选项
- gitPath,  git repository 本地路径
- branch, 分支名称， 可选