package gcp.project.cloud.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import gcp.project.cloud.model.Client;
import gcp.project.cloud.model.ClientRequiredDto;
import gcp.project.cloud.repository.BigQueryRepository;
import gcp.project.cloud.repository.GoogleStorageRepository;
import gcp.project.cloud.service.ClientRequiredDtoService;
import gcp.project.cloud.service.ClientService;
import gcp.project.cloud.service.parsing.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class ClientRequiredDtoServiceImplTest {
    private static final String FILE_CLIENT_REQUIRED =
            "src/test/resources/test_clients_required_upload.json";
    private static final String TEST_STRING = "Test";
    private ConvertObjectToDataService<ClientRequiredDto> objectToDataService;
    private List<Client> clientList;
    private ModelMapper modelMapper;
    private ObjectMapper objectMapper;
    private BigQueryRepository bigQueryRepository;
    private ClientRequiredDtoService clientRequiredDtoService;

    @Before
    public void configureDependencies() {
        bigQueryRepository = Mockito.mock(BigQueryRepository.class);
        objectMapper = new ObjectMapper();
        objectToDataService = new ClientRequiredToJsonConverterImpl(objectMapper);
        modelMapper = new ModelMapper();
        clientRequiredDtoService = new ClientRequiredDtoServiceImpl(objectToDataService,
                bigQueryRepository, modelMapper);
        clientList = List.of(new Client(1L, "John", "1234", "address"));
    }

    @Test
    public void getClientRequiredDtoTest() {
        List<ClientRequiredDto> actual = clientRequiredDtoService.getClientRequiredDto(clientList);
        List<ClientRequiredDto> expected = List.of(new ClientRequiredDto(1L, "John"));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void uploadClientsRequiredDtoTest() {
        List<ClientRequiredDto> clientRequiredDtos = List.of(new ClientRequiredDto(1L, "John"));
        clientRequiredDtoService.uploadClientsRequiredDto(clientRequiredDtos, TEST_STRING,
                TEST_STRING, FILE_CLIENT_REQUIRED);
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