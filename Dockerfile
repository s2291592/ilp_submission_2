FROM --platform=linux/amd64 openjdk:23

# Expose port 8080 for the application
EXPOSE 8080

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the build context (host machine) to the container
COPY ./target/ilp_submission_2-0.0.1-SNAPSHOT.jar app.jar

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
