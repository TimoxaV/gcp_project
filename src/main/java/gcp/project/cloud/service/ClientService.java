package gcp.project.cloud.service;

import gcp.project.cloud.model.Client;
import java.util.List;

public interface ClientService {
    List<Client> downloadClients(String storageBucketName, String storageObjectName,
                                 String clientsFromStorageAvro);

    void uploadClients(List<Client> clients, String jsonToUpload, String dataSet,
                       String clientsTable);
}
