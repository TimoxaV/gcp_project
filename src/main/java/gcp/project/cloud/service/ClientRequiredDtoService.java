package gcp.project.cloud.service;

import gcp.project.cloud.model.Client;
import gcp.project.cloud.model.ClientRequiredDto;
import java.util.List;

public interface ClientRequiredDtoService {
    List<ClientRequiredDto> getClientRequiredDto(List<Client> clients);

    void uploadClientsRequiredDto(List<ClientRequiredDto> clientRequiredDtos);
}
