# Use lightweight Java runtime
FROM maven:3.9.6-eclipse-temurin-21

# Set working directory
WORKDIR /app

# Copy jar file
COPY target/event-notification-system-1.0-SNAPSHOT.jar app.jar

# Expose application port
EXPOSE 8080

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]
