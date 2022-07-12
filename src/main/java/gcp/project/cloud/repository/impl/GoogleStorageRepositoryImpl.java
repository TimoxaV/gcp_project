//package gcp.project.cloud.repository.impl;
//
//import com.google.cloud.storage.Blob;
//import com.google.cloud.storage.BlobId;
//import com.google.cloud.storage.Storage;
//import gcp.project.cloud.repository.GoogleStorageRepository;
//import java.nio.file.Paths;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public class GoogleStorageRepositoryImpl implements GoogleStorageRepository {
//    private final Storage storage;
//
//    @Autowired
//    public GoogleStorageRepositoryImpl(Storage storage) {
//        this.storage = storage;
//    }
//
//    @Override
//    public void downloadObjectFromStorage(String bucketName, String objectName,
//                                          String destFilePath) {
//        Blob blob = storage.get(BlobId.of(bucketName, objectName));
//        blob.downloadTo(Paths.get(destFilePath));
//    }
//}
