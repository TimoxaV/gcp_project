package gcp.project.cloud.service.impl;

import gcp.project.cloud.model.Client;
import gcp.project.cloud.repository.BigQueryRepository;
import gcp.project.cloud.repository.GoogleStorageRepository;
import gcp.project.cloud.service.ClientService;
import gcp.project.cloud.service.parsing.AvroToClientConverterImpl;
import gcp.project.cloud.service.parsing.ClientToJsonConverterImpl;
import gcp.project.cloud.service.parsing.ConvertDataToObjectService;
import gcp.project.cloud.service.parsing.ConvertObjectToDataService;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

public class ClientServiceImplTest {
    private static final String TEST_CLIENTS_FROM_STORAGE =
            "src/test/resources/testClientsFromStorage.avro";
    private static final String FILE_CLIENT = "src/test/resources/test_clients_upload.json";
    private static final String TEST_STRING = "Test";
    private ConvertDataToObjectService<Client> dataToObjectService;
    private ConvertObjectToDataService<Client> objectToDataService;
    private List<Client> clientsExpected;
    private List<Client> clientsToUpload;
    private GoogleStorageRepository googleStorageRepository;
    private BigQueryRepository bigQueryRepository;
    private ClientService clientService;

    @Before
    public void configureDependencies() {
        googleStorageRepository = Mockito.mock(GoogleStorageRepository.class);
        bigQueryRepository = Mockito.mock(BigQueryRepository.class);
        dataToObjectService = new AvroToClientConverterImpl();
        objectToDataService = new ClientToJsonConverterImpl();
        clientService = new ClientServiceImpl(googleStorageRepository,
                bigQueryRepository, dataToObjectService, objectToDataService);
        clientsExpected = List.of(new Client(1L, "John", "1234", null),
                new Client(2L, "Ben", "4567", "Address one"),
                new Client(3L, "Bob", "7890", "address two"));
        clientsToUpload = List.of(new Client(1L, "John", "1234", "address"));
    }

    @Test
    public void downloadClientsTest() {
        List<Client> clientsActual = clientService.downloadClients(TEST_STRING, TEST_STRING, TEST_CLIENTS_FROM_STORAGE);
        verify(googleStorageRepository, Mockito.times(1))
                .downloadObjectFromStorage(any(), any(), any());
        Assert.assertEquals(clientsExpected, clientsActual);
    }

    @Test
    public void uploadClientsTest() {
        clientService.uploadClients(clientsToUpload, FILE_CLIENT, TEST_STRING, TEST_STRING);
        verify(bigQueryRepository, Mockito.times(1))
                .writeToTable(anyString(), anyString(), anyString());
        String expected = "{\"id\": 1, \"name\": \"John\", \"phone\": \"1234\", \"address\": \"address\"}";
        String actual = null;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_CLIENT));) {
            actual = bufferedReader.readLine();
        } catch (IOException e) {
            Assert.fail("File doesn't exist " + e);
        }
        Assert.assertEquals(expected, actual);
    }
}
