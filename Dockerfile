FROM openjdk:21-jdk

WORKDIR /app

COPY target/onlineStoreApi-0.0.1-SNAPSHOT.jar /app/onlineStoreApi.jar

EXPOSE 8080

CMD ["java", "-jar", "onlineStoreApi.jar"]