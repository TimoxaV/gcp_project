package gcp.project.cloud.service.parsing;

import com.fasterxml.jackson.databind.ObjectMapper;
import gcp.project.cloud.model.ClientRequiredDto;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ClientRequiredToJsonConverterImplTest {
    private static final String FILE_CLIENT_REQUIRED = "src/test/resources/test_clients_required_upload.json";
    private ConvertObjectToDataService<ClientRequiredDto> objectToDataService;
    private ObjectMapper objectMapper;
    private ClientRequiredDto clientRequiredDto;


    @Before
    public void createClientRequiredAndService() {
        objectMapper = new ObjectMapper();
        objectToDataService = new ClientRequiredToJsonConverterImpl(objectMapper);
        clientRequiredDto = new ClientRequiredDto(1L, "John");
    }

    @Test
    public void writeObjectToFileTest_Ok() {
        objectToDataService.writeObjectToFile(List.of(clientRequiredDto), FILE_CLIENT_REQUIRED);
        String expected = "{\"id\":1,\"name\":\"John\"}";
        String actual = null;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_CLIENT_REQUIRED))) {
            actual = bufferedReader.readLine();
        } catch (IOException e) {
            Assert.fail("File doesn't exist " + e);
        }
        Assert.assertEquals(expected, actual);
    }
}