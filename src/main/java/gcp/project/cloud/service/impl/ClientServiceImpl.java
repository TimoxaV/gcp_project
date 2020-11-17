package gcp.project.cloud.service.impl;

import gcp.project.cloud.model.Client;
import gcp.project.cloud.repository.BigQueryRepository;
import gcp.project.cloud.repository.GoogleStorageRepository;
import gcp.project.cloud.service.ClientService;
import gcp.project.cloud.service.parsing.ConvertDataToObjectService;
import gcp.project.cloud.service.parsing.ConvertObjectToDataService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {
    private final GoogleStorageRepository googleStorageRepository;
    private final BigQueryRepository bigQueryRepository;
    private final ConvertDataToObjectService<Client> dataToObjectService;
    private final ConvertObjectToDataService<Client> objectToDataService;
    @Value("${storage.bucket.name}")
    private String storageBucketName;
    @Value("${storage.object.name}")
    private String storageObjectName;
    @Value("${clients.from.storage.avro}")
    private String clientsFromStorageAvro;
    @Value("${data.set}")
    private String dataSet;
    @Value("${clients.table}")
    private String clientsTable;
    @Value("${json.to.upload}")
    private String jsonToUpload;

    @Autowired
    public ClientServiceImpl(GoogleStorageRepository googleStorageRepository,
                             BigQueryRepository bigQueryRepository,
                             ConvertDataToObjectService<Client> dataToObjectService,
                             ConvertObjectToDataService<Client> objectToDataService) {
        this.googleStorageRepository = googleStorageRepository;
        this.bigQueryRepository = bigQueryRepository;
        this.dataToObjectService = dataToObjectService;
        this.objectToDataService = objectToDataService;
    }

    @Override
    public List<Client> downloadClients() {
        googleStorageRepository.downloadObjectFromStorage(storageBucketName,
                storageObjectName, clientsFromStorageAvro);
        return dataToObjectService.parseFileToObject();
    }

    @Override
    public void uploadClients(List<Client> clients) {
        objectToDataService.writeObjectToFile(clients);
        bigQueryRepository.writeToTable(dataSet, clientsTable, jsonToUpload);
    }
}
