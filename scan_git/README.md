### 说明：
程序会扫描指定的 GIT 仓库， 并在命令当前目录下生成SQL文件 output.sql


### 构建：
-  `mvn clean package spring-boot:repackage -DskipTests`

### 运行：

`java -jar target/scan_git-*.jar --git-path=/somepath/gittest/ --branch=master  --after=1400000000000`

选项
- gitPath,  git repository 本地路径
- branch, 分支名称， 可选
- after, 时间戳， 仅仅扫描after 之后的提交


### 工具
src/main/resources/db/migration/db_recreate_and_load.sh ， 运行此工具新建DB，并导入 output.sql 到DB 