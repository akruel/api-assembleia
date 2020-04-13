FROM gradle:6.3.0-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:13-jdk-alpine

EXPOSE 8080

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/api-assembleia.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "/app/api-assembleia.jar"]

