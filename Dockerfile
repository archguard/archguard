FROM openjdk:12-jdk-alpine

RUN apk add --no-cache tzdata git curl && \
	cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
	echo "Asia/Shanghai" > /etc/timezone && \
	apk del tzdata

RUN addgroup -S spring && adduser -S spring -G spring

USER spring:spring

WORKDIR /home/spring

ARG SCAN_VERSION_ARG=1.6.2
ENV SCAN_VERSION=$SCAN_VERSION_ARG

ARG JAR_FILE=./server/build/libs/*.jar

COPY ${JAR_FILE} /home/spring/app.jar

# this is for save version for debug
COPY ["scanner_cli/build/libs/scanner_cli*-all.jar", "/home/spring/"]
# the really scanner jar
COPY ["scanner_cli/build/libs/scanner_cli*-all.jar", "/home/spring/scanner_cli.jar"]

RUN ["mkdir", "-p", "/home/spring/dependencies/analysers/"]

RUN chown -R spring:spring /home/spring/dependencies/analysers/

# copy features & langs & rules
COPY ["analyser_diff_changes/build/libs/analyser_diff_changes*-all.jar", \
"analyser_git/build/libs/analyser_git*-all.jar",                          \
"analyser_sca/build/libs/analyser_sca*-all.jar",                           \
"analyser_sourcecode/feat_apicalls/build/libs/feat_apicalls*-all.jar",      \
"analyser_sourcecode/feat_datamap/build/libs/feat_datamap*-all.jar",         \
"analyser_sourcecode/lang_csharp/build/libs/lang_csharp*-all.jar",            \
"analyser_sourcecode/lang_golang/build/libs/lang_golang*-all.jar",             \
"analyser_sourcecode/lang_java/build/libs/lang_java*-all.jar",                  \
"analyser_sourcecode/lang_kotlin/build/libs/lang_kotlin*-all.jar",               \
"analyser_sourcecode/lang_python/build/libs/lang_python*-all.jar",                \
"analyser_sourcecode/lang_scala/build/libs/lang_scala*-all.jar",                   \
"analyser_sourcecode/lang_typescript/build/libs/lang_typescript*-all.jar",          \
"rule-core/build/libs/rule-core*.jar",                                               \
"rule-linter/rule-sql/build/libs/rule-sql*.jar",                                      \
"rule-linter/rule-webapi/build/libs/rule-webapi*.jar",                                 \
"rule-linter/rule-test/build/libs/rule-test*.jar",                            \
# target directory
"/home/spring/dependencies/analysers/"]

ENTRYPOINT ["java","-jar","/home/spring/app.jar","--spring.profiles.active=${app_env}"]
