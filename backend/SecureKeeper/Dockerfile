FROM openjdk:25-ea-23-jdk-oraclelinux8

WORKDIR /app

COPY target/SecureKeeper-1.0.jar app.jar

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "app.jar"]