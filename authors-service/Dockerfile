FROM maven:3.6.3-adoptopenjdk-15 AS build
COPY src home/authors-service/src
COPY pom.xml home/authors-service
RUN mvn -f home/authors-service/pom.xml clean package -Dmaven.test.skip=true

FROM openjdk:15-alpine
COPY --from=build /home/authors-service/target/authors-service-1.0.0.jar /usr/local/lib/authors.jar
ENV DATABASE_USR="selectDbUser"
ENV DATABASE_PWD="selectDbPwd"
ENV DATABASE_NAME="selectDbName"
CMD ["java", "--enable-preview", "-jar", "/usr/local/lib/authors.jar"]