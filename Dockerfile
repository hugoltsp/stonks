FROM gradle:6.0.1-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build
FROM azul/zulu-openjdk-alpine:11-jre
EXPOSE 8080
COPY --from=build /home/gradle/src/build/libs/*[0-9].jar app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]