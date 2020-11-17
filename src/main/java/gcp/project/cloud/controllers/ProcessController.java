package gcp.project.cloud.controllers;

import gcp.project.cloud.model.Client;
import gcp.project.cloud.model.ClientRequiredDto;
import gcp.project.cloud.service.ClientRequiredDtoService;
import gcp.project.cloud.service.ClientService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProcessController {
    private final ClientService clientService;
    private final ClientRequiredDtoService clientRequiredDtoService;

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
        List<Client> clients = clientService.downloadClients();
        clientService.uploadClients(clients);
        List<ClientRequiredDto> clientRequiredDto =
                clientRequiredDtoService.getClientRequiredDto(clients);
        clientRequiredDtoService.uploadClientsRequiredDto(clientRequiredDto);
        return "All processed";
    }
}
