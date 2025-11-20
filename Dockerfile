FROM maven:3.9.9-ibm-semeru-23-jammy AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:23-ea-jdk-slim
COPY --from=build target/showcase-1.0.0.jar app.jar
COPY src/main/resources src/main/resources
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]