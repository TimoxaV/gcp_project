package gcp.project.cloud.service;

import gcp.project.cloud.model.Client;
import java.util.List;

public interface ClientService {
    List<Client> downloadClients();

    void uploadClients(List<Client> clients);
}
