FROM openjdk:11-jdk-slim-buster
COPY /build/libs/Habit-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=scheduler", "-Djasypt.encryptor.password=OUR-PASSWORD", "/app.jar"]