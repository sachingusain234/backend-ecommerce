FROM maven:3.9.5-openjdk-21 as build
COPY . .
RUN mvn clean package -DskipTests
FROM openjdk:21-slim
COPY --from=build /target/Spring-security-jwt-0.0.1-SNAPSHOT.jar Spring-security-jwt.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","Spring-security-jwt.jar"]