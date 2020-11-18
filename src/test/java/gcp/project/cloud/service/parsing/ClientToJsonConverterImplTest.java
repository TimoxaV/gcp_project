package gcp.project.cloud.service.parsing;

import gcp.project.cloud.model.Client;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ClientToJsonConverterImplTest {
    private static final String FILE_CLIENT = "src/test/resources/test_clients_upload.json";
    private ConvertObjectToDataService<Client> objectToDataService;
    private Client client;

    @Before
    public void createClientAndService() {
        objectToDataService = new ClientToJsonConverterImpl();
        client = new Client(1L, "John", "1234", "address");
    }

    @Test
    public void writeObjectToFileTest_Ok() throws IOException {
        String expected = "{\"id\": 1, \"name\": \"John\", \"phone\": \"1234\", \"address\": \"address\"}";
        objectToDataService.writeObjectToFile(List.of(client), FILE_CLIENT);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_CLIENT));
        String actual = bufferedReader.readLine();
        bufferedReader.close();
        Assert.assertEquals(expected, actual);
    }
}