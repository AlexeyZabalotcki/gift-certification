FROM openjdk:17
WORKDIR /app
COPY build/libs/gift-certificates-task-1.0-SNAPSHOT.war app.war
CMD ["java", "-jar", "app.war"]
