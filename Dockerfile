FROM openjdk:8-jdk

EXPOSE 8080

ADD . /code

WORKDIR /code

RUN ls -la

RUN chmod u+x gradlew && ./gradlew clean build -x check && cp build/libs/ReviewsApi.jar /app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","-Dspring.profiles.active=docker","/app.jar"]