#Build Stage
FROM maven:3.8.4-openjdk-11-slim as mvnBuild
WORKDIR /build
COPY .mvn .mvn
COPY mvnw .
COPY mvnw.cmd .
COPY src src
COPY pom.xml .
RUN mvn clean compile package

#Runtime Stage
FROM openjdk:11-jre-slim-buster
WORKDIR /app
COPY --from=mvnbuild /build/target/*jar heroesmd.jar

EXPOSE 8080

CMD ["java" ,"-jar", "heroesmd.jar"]