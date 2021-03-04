FROM maven:3.6.3-adoptopenjdk-15 AS build
COPY src home/gateway-service-jwt/src
COPY pom.xml home/gateway-service-jwt
RUN mvn -f home/gateway-service-jwt/pom.xml clean package -Dmaven.test.skip=true

FROM openjdk:15-alpine
COPY --from=build /home/gateway-service-jwt/target/gateway-service-jwt-1.0.0.jar /usr/local/lib/gateway.jar
CMD ["java", "-jar", "/usr/local/lib/gateway.jar"]