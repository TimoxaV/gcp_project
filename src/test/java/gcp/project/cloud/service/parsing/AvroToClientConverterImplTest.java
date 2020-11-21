package gcp.project.cloud.service.parsing;

import gcp.project.cloud.exceptions.DataProcessException;
import gcp.project.cloud.model.Client;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class AvroToClientConverterImplTest {
    private ConvertDataToObjectService<Client> dataToObjectService;
    private static final String TEST_CLIENTS_FROM_AVRO =
            "src/test/resources/testClientsFromStorage.avro";
    private static final String TEST_NO_FILE =
            "src/test/resources/testClients.avro";

    @Before
    public void createService() {
         dataToObjectService = new AvroToClientConverterImpl();
    }

    @Test
    public void parseFileToObject_OK() {
        ConvertDataToObjectService<Client> dataToObjectService = new AvroToClientConverterImpl();
        List<Client> clientsExpected = List.of(new Client(1L, "John", "1234", null),
                new Client(2L, "Ben", "4567", "Address one"),
                new Client(3L, "Bob", "7890", "address two"));
        List<Client> clientsActual = dataToObjectService.parseFileToObject(TEST_CLIENTS_FROM_AVRO);
        Assert.assertEquals(clientsExpected, clientsActual);
    }

    @Test(expected = DataProcessException.class)
    public void parseFileToObjectNoFileTest() {
        dataToObjectService.parseFileToObject(TEST_NO_FILE);
    }
}
