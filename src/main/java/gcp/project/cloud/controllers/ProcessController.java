//package gcp.project.cloud.controllers;
//
//import gcp.project.cloud.dto.NotificationDto;
//import gcp.project.cloud.model.Client;
//import gcp.project.cloud.model.ClientRequiredInfo;
//import gcp.project.cloud.service.ClientRequiredInfoService;
//import gcp.project.cloud.service.ClientService;
//import java.util.List;
//import java.util.Map;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//import javax.validation.Valid;
//
//@RestController
//public class ProcessController {
//    private static final String BUCKET_ATTRIBUTE = "bucketId";
//    private static final String OBJECT_ATTRIBUTE = "objectId";
//    private final ClientService clientService;
//    private final ClientRequiredInfoService clientRequiredInfoService;
//    private final Logger logger = LoggerFactory.getLogger(ProcessController.class);
//    @Value("${clients.from.storage.avro}")
//    private String clientsFromStorageAvro;
//    @Value("${data.set}")
//    private String dataSet;
//    @Value("${clients.table}")
//    private String clientsTable;
//    @Value("${json.to.upload}")
//    private String jsonToUpload;
//    @Value("${clients.required.table}")
//    private String clientsRequiredTable;
//    @Value("${json.to.upload.required.fields}")
//    private String requiredJsonToUpload;
//
//    @Autowired
//    public ProcessController(ClientService clientService,
//                             ClientRequiredInfoService clientRequiredInfoService) {
//        this.clientService = clientService;
//        this.clientRequiredInfoService = clientRequiredInfoService;
//    }
//
//    @GetMapping()
//    public String hello() {
//        return "Hello";
//    }
//
//    @PostMapping("/process")
//    public ResponseEntity<String> postProcessData(@RequestBody @Valid NotificationDto notificationDto) {
//        logger.info("Hello from POST process");
//        Map<String, String> attributes = notificationDto.getMessage().getAttributes();
//        String bucket = attributes.get(BUCKET_ATTRIBUTE);
//        String object = attributes.get(OBJECT_ATTRIBUTE);
//        logger.info("Start processing: " + bucket + " / " + object);
//        List<Client> clients = clientService.downloadClients(bucket, object,
//                clientsFromStorageAvro);
//        clientService.uploadClients(clients, jsonToUpload, dataSet, clientsTable);
//        List<ClientRequiredInfo> clientRequiredDto =
//                clientRequiredInfoService.getClientRequiredDto(clients);
//        clientRequiredInfoService.uploadClientsRequiredInfo(clientRequiredDto, dataSet,
//                clientsRequiredTable, requiredJsonToUpload);
//        String body = "Finish processing: " + bucket + " / " + object;
//        logger.info(body);
//        return new ResponseEntity<>(body, HttpStatus.OK);
//    }
//}
