FROM openjdk:8-jdk

EXPOSE 8080

ADD ./reviews /code
WORKDIR /code

RUN ./gradlew clean build -x check  && cp /code/build/libs/ReviewsApi.jar /app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar", "Dspring.profiles.active=docker,"/app.jar"]