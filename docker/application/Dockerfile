FROM maven:3.9.11-eclipse-temurin-21 AS builder
ARG SOURCE_PATH=..
WORKDIR /home/app
COPY ${SOURCE_PATH}/ .
RUN ls -laR
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine AS optimizer
ARG JAR_FILE=/home/app/target/*.jar
WORKDIR /home/app
COPY --from=builder ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract
RUN ls -laR

FROM eclipse-temurin:21-jre-alpine
MAINTAINER Christian Schiebler

COPY --from=optimizer /home/app/dependencies/ ./
COPY --from=optimizer /home/app/snapshot-dependencies/ ./
COPY --from=optimizer /home/app/spring-boot-loader/ ./
COPY --from=optimizer /home/app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]