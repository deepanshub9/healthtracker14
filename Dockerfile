FROM eclipse-temurin:21-jdk


WORKDIR /app


# Copy the fat JAR into the container
COPY . .


# Expose the port used by your application
EXPOSE 8001


# Run the application
CMD ["java", "-jar", "target/health-tracker-rest-1.0-SNAPSHOT-jar-with-dependencies.jar"]
