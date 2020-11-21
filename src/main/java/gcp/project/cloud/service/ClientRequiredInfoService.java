package gcp.project.cloud.service;

import gcp.project.cloud.model.Client;
import gcp.project.cloud.model.ClientRequiredInfo;
import java.util.List;

public interface ClientRequiredInfoService {
    List<ClientRequiredInfo> getClientRequiredDto(List<Client> clients);

    void uploadClientsRequiredInfo(List<ClientRequiredInfo> clientRequiredInfos, String dataSet,
                                   String clientsRequiredTable, String requiredJsonToUpload);
}
