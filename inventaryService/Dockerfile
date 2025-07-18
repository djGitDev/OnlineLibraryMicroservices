# Étape de build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Étape d'exécution
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar ./app.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar" , "app.jar", "--server.port=8082"]

