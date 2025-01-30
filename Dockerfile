# Use Amazon Corretto JDK 17 as base image
FROM amazoncorretto:17

# Set the working directory
WORKDIR /app

# Copy the JAR file from the target folder
COPY target/foodie-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8080

# Add a delay to ensure MySQL is up before starting the backend
CMD ["sh", "-c", "sleep 10 && java -jar app.jar"]
