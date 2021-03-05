FROM openjdk:15-alpine
COPY target/service-finder-1.0.0.jar /usr/local/lib/servicefinder.jar
CMD ["java", "--enable-preview", "-jar", "/usr/local/lib/servicefinder.jar"]