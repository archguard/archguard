FROM openjdk:8-jdk-alpine

RUN apk add --no-cache tzdata && \
	cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
	echo "Asia/Shanghai" > /etc/timezone && \
	apk del tzdata

RUN addgroup -S spring && adduser -S spring -G spring

USER spring:spring

ARG JAR_FILE=./build/libs/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=${app_env}"]
