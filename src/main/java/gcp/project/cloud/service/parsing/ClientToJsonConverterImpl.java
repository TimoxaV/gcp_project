package gcp.project.cloud.service.parsing;

import gcp.project.cloud.exceptions.DataProcessException;
import gcp.project.cloud.model.Client;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ClientToJsonConverterImpl implements ConvertObjectToDataService<Client> {

    @Override
    public void writeObjectToFile(List<Client> clients, String filePath) {
        try {
            FileWriter fileWriter = new FileWriter(new File(filePath));
            for (Client client : clients) {
                fileWriter.append(client.toString());
                fileWriter.append("\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            throw new DataProcessException("Can't write clients to json", e);
        }
    }
}
