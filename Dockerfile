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

#COPY ./analyser_architecture/build/libs/*.jar /home/spring/dependencies/analysers/

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
                                                                                     \
"rule-linter/rule-sql/build/libs/rule-sql*.jar",                                        \
"rule-linter/rule-webapi/build/libs/rule-webapi*.jar",                                   \
"rule-linter/rule-test-code/build/libs/rule-test-code*.jar",                              \

# target directory
"/home/spring/dependencies/analysers/"]

COPY [
"scanner_cli/build/libs/scanner_cli*-all.jar",

# target directory
"/home/spring/"]


#ADD --chown=spring:spring https://github.com/archguard/scanner/releases/download/v${SCAN_VERSION}/analyser_sca-${SCAN_VERSION}-all.jar .
#ADD --chown=spring:spring https://github.com/archguard/scanner/releases/download/v${SCAN_VERSION}/diff_changes-${SCAN_VERSION}-all.jar .
#ADD --chown=spring:spring https://github.com/archguard/scanner/releases/download/v${SCAN_VERSION}/scan_git-${SCAN_VERSION}-all.jar .
#ADD --chown=spring:spring https://github.com/archguard/scanner/releases/download/v${SCAN_VERSION}/scan_sourcecode-${SCAN_VERSION}-all.jar .
#ADD --chown=spring:spring https://github.com/archguard/scanner/releases/download/v${SCAN_VERSION}/scan_jacoco-${SCAN_VERSION}-all.jar .
#ADD --chown=spring:spring https://github.com/archguard/scanner/releases/download/v${SCAN_VERSION}/scan_test_badsmell-${SCAN_VERSION}-all.jar .

ENTRYPOINT ["java","-jar","/home/spring/app.jar","--spring.profiles.active=${app_env}"]
