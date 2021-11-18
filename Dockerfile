FROM openjdk:11-jdk-slim-buster
COPY ~/build/libs/Habit-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "-Djasypt.encryptor.password=OUR-PASSWORD", "/app.jar"]