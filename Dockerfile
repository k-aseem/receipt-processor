# Stage 1: Build the application using Maven and Amazon Corretto 17
FROM maven:3.9-amazoncorretto-17 AS builder
WORKDIR /app
# Copy all source code and pom.xml into the container
COPY . .
# Build the project and package the jar
RUN mvn clean package -DskipTests

# Stage 2: Create a minimal runtime image using Amazon Corretto 17 Alpine
FROM amazoncorretto:17-alpine
WORKDIR /app
# Copy the packaged jar from the builder stage
COPY --from=builder /app/target/receipt-processor-0.0.1-SNAPSHOT.jar app.jar
# Expose port 8080
EXPOSE 8080
# Set the entry point to run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
