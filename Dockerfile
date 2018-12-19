FROM openjdk:8-jdk

EXPOSE 8080

ADD ReviewsApi.jar /app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar", "Dspring.profiles.active=docker,"/app.jar"]