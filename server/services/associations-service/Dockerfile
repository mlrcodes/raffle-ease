FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} associations-service.jar
ENTRYPOINT ["java", "-jar", "associations-service.jar"]
