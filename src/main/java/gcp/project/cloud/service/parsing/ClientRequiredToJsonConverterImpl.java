package gcp.project.cloud.service.parsing;

import com.fasterxml.jackson.databind.ObjectMapper;
import gcp.project.cloud.exceptions.DataProcessException;
import gcp.project.cloud.model.ClientRequiredDto;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientRequiredToJsonConverterImpl implements
        ConvertObjectToDataService<ClientRequiredDto> {
    private final ObjectMapper objectMapper;

    @Autowired
    public ClientRequiredToJsonConverterImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void writeObjectToFile(List<ClientRequiredDto> clientRequiredDtos, String filePath) {
        try {
            FileWriter fileWriter = new FileWriter(new File(filePath));
            for (ClientRequiredDto clientRequiredDto : clientRequiredDtos) {
                fileWriter.write(objectMapper.writeValueAsString(clientRequiredDto));
                fileWriter.write("\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            throw new DataProcessException("Can't write clients required fields to json", e);
        }
    }
}
