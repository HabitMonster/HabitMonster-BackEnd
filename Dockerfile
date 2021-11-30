FROM openjdk:11-jdk-slim-buster
COPY /build/libs/Habit-0.0.1-SNAPSHOT.jar app.jar
ENV TZ Asia/Seoul
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Djasypt.encryptor.password=OUR-PASSWORD", "-Dspring.profiles.active=scheduler", "/app.jar"]