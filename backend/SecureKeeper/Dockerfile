FROM openjdk:25-ea-23-jdk-oraclelinux8

WORKDIR /app

COPY mvnw .
COPY .mvn/ .mvn
COPY pom.xml .

COPY src/ src/

RUN ./mvnw clean package -DskipTests

COPY target/SecureKeeper-1.0.jar app.jar

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "target/SecureKeeper-1.0.jar"]