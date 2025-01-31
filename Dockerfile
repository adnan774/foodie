# Use Amazon Corretto as base image
FROM amazoncorretto:17 AS build

# Set working directory
WORKDIR /app

# Copy only the built JAR file (avoid sending unnecessary files)
COPY target/foodie-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]
