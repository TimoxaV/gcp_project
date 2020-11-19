package gcp.project.cloud.controllers;

import gcp.project.cloud.dto.StorageDto;
import gcp.project.cloud.model.Client;
import gcp.project.cloud.model.ClientRequiredDto;
import gcp.project.cloud.service.ClientRequiredDtoService;
import gcp.project.cloud.service.ClientService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProcessController {
    private final ClientService clientService;
    private final ClientRequiredDtoService clientRequiredDtoService;
    private final Logger logger = LoggerFactory.getLogger(ProcessController.class);
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

    @PostMapping("/process")
    public String postProcessData(@RequestBody StorageDto storageDto) {
        logger.info("Hello from POST process");
        String bucket = storageDto.getBucketName();
        String object = storageDto.getObjectName();
        List<Client> clients = clientService.downloadClients(bucket, object,
                clientsFromStorageAvro);
        clientService.uploadClients(clients, jsonToUpload, dataSet, clientsTable);
        List<ClientRequiredDto> clientRequiredDto =
                clientRequiredDtoService.getClientRequiredDto(clients);
        clientRequiredDtoService.uploadClientsRequiredDto(clientRequiredDto, dataSet,
                clientsRequiredTable, requiredJsonToUpload);
        return "All processed Post";
    }
}
