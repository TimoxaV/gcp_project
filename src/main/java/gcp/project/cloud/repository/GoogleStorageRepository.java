package gcp.project.cloud.repository;

public interface GoogleStorageRepository {
    void downloadObjectFromStorage(String bucketName, String objectName, String destFilePath);
}
