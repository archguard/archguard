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



#ADD --chown=spring:spring https://github.com/archguard/scanner/releases/download/v${SCAN_VERSION}/analyser_sca-${SCAN_VERSION}-all.jar .
#ADD --chown=spring:spring https://github.com/archguard/scanner/releases/download/v${SCAN_VERSION}/diff_changes-${SCAN_VERSION}-all.jar .
#ADD --chown=spring:spring https://github.com/archguard/scanner/releases/download/v${SCAN_VERSION}/scan_git-${SCAN_VERSION}-all.jar .
#ADD --chown=spring:spring https://github.com/archguard/scanner/releases/download/v${SCAN_VERSION}/scan_sourcecode-${SCAN_VERSION}-all.jar .
#ADD --chown=spring:spring https://github.com/archguard/scanner/releases/download/v${SCAN_VERSION}/scan_jacoco-${SCAN_VERSION}-all.jar .
#ADD --chown=spring:spring https://github.com/archguard/scanner/releases/download/v${SCAN_VERSION}/scan_test_badsmell-${SCAN_VERSION}-all.jar .

ENTRYPOINT ["java","-jar","/home/spring/app.jar","--spring.profiles.active=${app_env}"]
