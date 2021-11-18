FROM openjdk:11-jdk-slim-buster
ADD /build/libs/Habit-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "-Djasypt.encryptor.password=OUR-PASSWORD", "/app.jar"]