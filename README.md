### **GCP Project**

**Implemented basic data exchange process between Google Storage and Google BigQuery** 

1) Downloading avro file from Storage
2) Parsing avro files to objects
3) Write all objects to JSON
4) Upload data in JSON file to BigQuery 

**Used technologies:**

- Spring Boot
- Google Storage
- Google BigQuery
- Cloud Run
- Mockito
- Docker
- REST

**To run the project on your local machine:**

1) Place JSON file with your GCP credentials into resource folder
2) Run in terminal mvn clean package
3) Build docker image: docker build -t *"DockerHubID"/"ImageName"* **.**
4) Run docker container: docker run -p 8080:8080 *"DockerHubID"/"ImageName"*
5) Visit http://localhost:8080/ in your browser
