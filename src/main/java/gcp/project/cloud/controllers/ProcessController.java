package gcp.project.cloud.controllers;

import gcp.project.cloud.dto.DataAttributesDto;
import gcp.project.cloud.dto.NotificationDto;
import gcp.project.cloud.model.Client;
import gcp.project.cloud.model.ClientRequiredInfo;
import gcp.project.cloud.service.ClientRequiredInfoService;
import gcp.project.cloud.service.ClientService;
import gcp.project.cloud.service.PubSubService;
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
    private final ClientRequiredInfoService clientRequiredInfoService;
    private final PubSubService pubSubService;
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
                             ClientRequiredInfoService clientRequiredInfoService,
                             PubSubService pubSubService) {
        this.clientService = clientService;
        this.clientRequiredInfoService = clientRequiredInfoService;
        this.pubSubService = pubSubService;
    }

    @GetMapping()
    public String hello() {
        return "Hello";
    }

    @PostMapping("/process")
    public String postProcessData(@RequestBody NotificationDto notificationDto) {
        logger.info("Hello from POST process");
        DataAttributesDto attributes = pubSubService.getDataAttributes(notificationDto);
        String bucket = attributes.getBucket();
        String object = attributes.getName();
        logger.info("Start processing: " + bucket + " / " + object);
        List<Client> clients = clientService.downloadClients(bucket, object,
                clientsFromStorageAvro);
        clientService.uploadClients(clients, jsonToUpload, dataSet, clientsTable);
        List<ClientRequiredInfo> clientRequiredDto =
                clientRequiredInfoService.getClientRequiredDto(clients);
        clientRequiredInfoService.uploadClientsRequiredInfo(clientRequiredDto, dataSet,
                clientsRequiredTable, requiredJsonToUpload);
        logger.info("Finish processing: " + bucket + " / " + object);
        return "All processed Post";
    }
}
