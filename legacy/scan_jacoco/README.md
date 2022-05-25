### 说明：

扫描目标项目下的 jacoco.exec 文件， 将目标项目的覆盖率数据生成 SQL 文件 output.sql 

### 构建：


### 运行：

`java -jar target/scan_jacoco-*.jar --target-project=/path/of/target/project --bin=target/classes  --exec=target/jacoco.exec`

选项
- target-project,  目标项目的绝对滤镜
- bin, 目标项目的字节码路径，相对路径，缺省 target/classes 
- exec, jacoco 文件路径， 缺省 target/jacoco.exec 


### 工具
