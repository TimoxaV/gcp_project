FROM maven:3.6-jdk-11 as builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests
FROM adoptopenjdk/openjdk11:alpine-slim
COPY --from=builder /app/target/cloud-*.jar /cloud.jar
COPY --from=builder /app/target/classes/clients_required_upload.json /home/clients_required_upload.json
COPY --from=builder /app/target/classes/clients_upload.json /home/clients_upload.json
COPY --from=builder /app/target/classes/clientsFromStorage.avro /home/clientsFromStorage.avro
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/cloud.jar"]
