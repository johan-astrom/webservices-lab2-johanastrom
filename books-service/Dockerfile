FROM openjdk:15-alpine
COPY target/books-service-1.0.0.jar /usr/local/lib/books.jar
ENV DATABASE_USR="selectDbUser"
ENV DATABASE_PWD="selectDbPwd"
ENV DATABASE_NAME="selectDbName"
CMD ["java", "--enable-preview", "-jar", "/usr/local/lib/books.jar"]