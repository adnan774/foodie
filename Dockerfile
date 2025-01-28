# Use Amazon Corretto as the base image
FROM amazoncorretto:17

# Add a build argument for the JAR file name
ARG JAR_FILE

# Copy the JAR file into the Docker image
COPY ${JAR_FILE} app.jar

# Expose the application port
EXPOSE 8080

# Set the command to run the application
CMD ["java", "-jar", "app.jar"]
