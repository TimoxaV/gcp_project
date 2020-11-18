package gcp.project.cloud.controllers;

import gcp.project.cloud.model.Client;
import gcp.project.cloud.model.ClientRequiredDto;
import gcp.project.cloud.service.ClientRequiredDtoService;
import gcp.project.cloud.service.ClientService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProcessController {
    private final ClientService clientService;
    private final ClientRequiredDtoService clientRequiredDtoService;
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
    @Value("${clients.required.table}")
    private String clientsRequiredTable;
    @Value("${json.to.upload.required.fields}")
    private String requiredJsonToUpload;

    @Autowired
    public ProcessController(ClientService clientService,
                             ClientRequiredDtoService clientRequiredDtoService) {
        this.clientService = clientService;
        this.clientRequiredDtoService = clientRequiredDtoService;
    }

    @GetMapping()
    public String hello() {
        return "Hello";
    }

    @GetMapping("/process")
    public String process() {
        List<Client> clients = clientService.downloadClients(storageBucketName, storageObjectName,
                clientsFromStorageAvro);
        clientService.uploadClients(clients, jsonToUpload, dataSet, clientsTable);
        List<ClientRequiredDto> clientRequiredDto =
                clientRequiredDtoService.getClientRequiredDto(clients);
        clientRequiredDtoService.uploadClientsRequiredDto(clientRequiredDto, dataSet,
                clientsRequiredTable, requiredJsonToUpload);
        return "All processed";
    }
}
