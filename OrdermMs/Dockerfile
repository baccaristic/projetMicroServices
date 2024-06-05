FROM openjdk:17
EXPOSE 9013
ADD target/OrdermMs-0.0.1-SNAPSHOT.jar Order_MS-docker.jar
ENTRYPOINT ["java","-jar","/Order_MS-docker.jar"]