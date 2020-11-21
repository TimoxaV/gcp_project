### **GCP Project**

**Implemented basic data exchange process between Google Storage and Google BigQuery** 

Cloud Function triggers creating an object in Google Storage and send request to start processing:
1) Downloading avro file from Storage
2) Parsing avro files to objects
3) Write all objects to JSON
4) Upload data in JSON file to BigQuery 

**Used technologies:**

- Spring Boot
- Google Storage
- Google BigQuery
- Cloud Run
- Pub/Sub
- Mockito
- Docker
- REST

**To run the project on your local machine:**

1) Place JSON file with your GCP credentials into resource folder
2) Run in terminal mvn clean package
3) Build docker image: docker build -t *DockerHubID/ImageName* **.**
4) Run docker container: docker run -p 8080:8080 *DockerHubID/ImageName*
5) Visit http://localhost:8080/ in your browser

**To run the project on Cloud Run:**
  
1) Place JSON file with your GCP credentials into resource folder
2) Run in terminal mvn clean package
3) Build and upload container to GCP: gcloud builds submit --tag gcr.io/*YourProjectId*/*ContainerName*
4) Create Cloud Run service using the container created on step 3
5) Create a bucket in your Google Storage, Pub/Sub topic and connected subscription
6) To make Storage send notifications on create-update event to the topic run this command in your Google Cloud Shell: gsutil notification create -f json -t projects/*YourProjectId*/topics/*YourTopicName* -e OBJECT_FINALIZE gs://gcp_project_test_bucket
