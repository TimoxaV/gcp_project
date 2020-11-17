# Use the official maven/Java 8 image to create a build artifact.
# https://hub.docker.com/_/maven
FROM maven:3.6-jdk-11 as builder

# Copy local code to the container image.
WORKDIR /app
COPY pom.xml .
COPY src ./src
#ADD src/main/resources/gcred.json /gcred.json

# Build a release artifact.
RUN mvn package -DskipTests

# Use AdoptOpenJDK for base image.
# It's important to use OpenJDK 8u191 or above that has container support enabled.
# https://hub.docker.com/r/adoptopenjdk/openjdk8
# https://docs.docker.com/develop/develop-images/multistage-build/#use-multi-stage-builds
FROM adoptopenjdk/openjdk11:alpine-slim

# Copy the jar and needed files to the production image from the builder stage.
COPY --from=builder /app/target/cloud-*.jar /cloud.jar
COPY --from=builder /app/target/classes/clients_required_upload.json /home/clients_required_upload.json
COPY --from=builder /app/target/classes/clients_upload.json /home/clients_upload.json
COPY --from=builder /app/target/classes/clientsFromStorage.avro /home/clientsFromStorage.avro

# Run the web service on container startup.
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/cloud.jar"]
