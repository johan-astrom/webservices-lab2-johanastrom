FROM openjdk:15-alpine
COPY target/gateway-service-jwt-1.0.0.jar /usr/local/lib/gateway.jar
CMD ["java", "--enable-preview", "-jar", "/usr/local/lib/gateway.jar"]