FROM openjdk:8-jdk

EXPOSE 8080

ADD . /code

WORKDIR /code


RUN apt-get install wget unzip -y

RUN ls -la && \
    wget https://services.gradle.org/distributions/gradle-4.10.3-bin.zip && \
    mkdir /opt/gradle && \
    unzip -d /opt/gradle gradle-4.10.3-bin.zip && \
    ls /opt/gradle/gradle-4.10.3

RUN export PATH=$PATH:/opt/gradle/gradle-4.10.3/bin

RUN chmod u+x gradle && gradle clean build -x check && cp build/libs/ReviewsApi.jar /app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","-Dspring.profiles.active=docker","/app.jar"]