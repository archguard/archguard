FROM openjdk:8-jdk-alpine

RUN apk add --no-cache tzdata && \
	cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
	echo "Asia/Shanghai" > /etc/timezone && \
	apk del tzdata

RUN addgroup -S spring && adduser -S spring -G spring

RUN apk add --no-cache git

USER spring:spring

ARG JAR_FILE=./build/libs/*.jar

COPY ${JAR_FILE} /home/spring/app.jar

ENTRYPOINT ["java","-jar","/home/spring/app.jar","--spring.profiles.active=${app_env}"]
