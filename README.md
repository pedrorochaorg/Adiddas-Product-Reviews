# Adidas Product Reviews Api
Exercise to show case skills when developing java enabled microservices.


## Built With

- Java 8
- Spring Boot 2.1.1
- Spring 5 Webflux
- Mongodb 3.X
- Docker / Docker Compose 18.09.0
- IntelliJ
- Project Lombok
- TestNG
- Jacoco

## Requirements
To run/build this code your host must have:
- Java 8 JRE/JDK
- Gradle 4.6+
- Docker/Docker Compose 18.00.0+


## Additional Information

This api supports 2 types of authentication, Basic Auth and Api Key authentication.

Api docs OpenApi format are accessible to the following urls:

- `http://<host>:8080/v2/api-docs`
- `http://<host>:8080/swagger-ui-html`


## Building the code

To build the code you must run the following command:

```bash
./gradlew clean build
```

## Running the Web Server

To run the webserver you must run the following command:
```bash
./gradlew clean bootRun
```
A Docker compose file will be run before the build starts and before the server starts running,
containing a running instance of the mongodb server. The webserver will start running on port 8080


## Running Tests
To run the test cases in this project you must run the following command:
```bash
./gradlew clean test
```
A Docker compose file will be run before the build starts and before the server starts running,
containing a running instance of the mongodb server.

The result of these tests will be stored in the following directories:

- reports/coverage (Jacoco Coverage Report)
- reports/tests/html ( TestNG / ReportNG Test Reports)


