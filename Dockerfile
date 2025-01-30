# Use Amazon Corretto as the base image
FROM amazoncorretto:17

# Set working directory
WORKDIR /app

# Copy only the JAR file (avoid sending unnecessary files)
COPY target/foodie-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8080

# Set the command to run the application
CMD ["java", "-jar", "app.jar"]
