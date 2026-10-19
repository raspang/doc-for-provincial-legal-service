# syntax=docker/dockerfile:1

FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY . .
RUN mvn -q -DskipTests dependency:go-offline
RUN mvn -q -DskipTests -Pprod package

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /workspace/target/legal-1.0.0.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
