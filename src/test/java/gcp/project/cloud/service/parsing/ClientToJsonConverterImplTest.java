package gcp.project.cloud.service.parsing;

import gcp.project.cloud.exceptions.DataProcessException;
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
    private static final String NO_FILE = "C::/Documents/testfile.json";
    private ConvertObjectToDataService<Client> objectToDataService;
    private Client client;

    @Before
    public void createClientAndService() {
        objectToDataService = new ClientToJsonConverterImpl();
        client = new Client(1L, "John", "1234", "address");
    }

    @Test
    public void writeObjectToFileTest_Ok() {
        String expected = "{\"id\": 1, \"name\": \"John\", \"phone\": \"1234\", \"address\": \"address\"}";
        objectToDataService.writeObjectToFile(List.of(client), FILE_CLIENT);
        String actual = null;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_CLIENT))) {
            actual = bufferedReader.readLine();
        } catch (IOException e) {
            Assert.fail("File doesn't exist " + e);
        }
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = DataProcessException.class)
    public void writeObjectToFileTestNoFile() {
        objectToDataService.writeObjectToFile(List.of(client), NO_FILE);
    }
}
