FROM maven:3.9.9-eclipse-temurin-23 AS build

COPY src/main .
RUN mvn clean package -DskipTests
FROM maven:3.9.9-eclipse-temurin-23-alpine
COPY --from=build /target/*.jar houseOfNature.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "houseOfNature.jar"]
