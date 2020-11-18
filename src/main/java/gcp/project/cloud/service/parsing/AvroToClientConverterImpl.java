package gcp.project.cloud.service.parsing;

import gcp.project.cloud.exceptions.DataProcessException;
import gcp.project.cloud.model.Client;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.io.DatumReader;
import org.apache.avro.specific.SpecificDatumReader;
import org.springframework.stereotype.Component;

@Component
public class AvroToClientConverterImpl implements ConvertDataToObjectService<Client> {

    @Override
    public List<Client> parseFileToObject(String filePath) {
        DatumReader<Client> clientDatumReader = new SpecificDatumReader<>(Client.class);
        List<Client> clients = new ArrayList<>();
        try {
            DataFileReader<Client> fileReader = new DataFileReader<>(new File(filePath),
                    clientDatumReader);
            while (fileReader.hasNext()) {
                clients.add(fileReader.next());
            }
            fileReader.close();
        } catch (IOException e) {
            throw new DataProcessException("Can't parse file data to client", e);
        }
        return clients;
    }
}
