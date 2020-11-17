package gcp.project.cloud;

import gcp.project.cloud.model.Client;
import gcp.project.cloud.model.ClientRequiredDto;
import gcp.project.cloud.service.ClientRequiredDtoService;
import gcp.project.cloud.service.ClientService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RunApplication {
    private final ClientService clientService;
    private final ClientRequiredDtoService clientRequiredDtoService;

    @Autowired
    public RunApplication(ClientService clientService,
                          ClientRequiredDtoService clientRequiredDtoService) {
        this.clientService = clientService;
        this.clientRequiredDtoService = clientRequiredDtoService;
    }

    public void run() {
        List<Client> clients = clientService.downloadClients();
        clientService.uploadClients(clients);
        List<ClientRequiredDto> clientRequiredDto =
                clientRequiredDtoService.getClientRequiredDto(clients);
        clientRequiredDtoService.uploadClientsRequiredDto(clientRequiredDto);
    }
}
