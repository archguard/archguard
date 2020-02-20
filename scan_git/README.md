### 说明：
程序会扫描指定的 GIT 仓库， 并在命令当前目录下生成SQL文件 output.sql

### 构建：
-  `mvn clean package spring-boot:repackage -DskipTests`

### 运行：

`java -jar target/scan_git-*.jar --gitPath=/somepath/gittest/ --branch=master`

选项
- gitPath,  git repository 本地路径
- branch, 分支名称， 可选