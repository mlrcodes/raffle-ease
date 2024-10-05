FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} discovery-server.jar
ENTRYPOINT ["java","-jar","/discovery-server.jar"]