//package gcp.project.cloud.service.impl;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import gcp.project.cloud.model.Client;
//import gcp.project.cloud.model.ClientRequiredInfo;
//import gcp.project.cloud.repository.BigQueryRepository;
//import gcp.project.cloud.service.ClientRequiredInfoService;
//import gcp.project.cloud.service.parsing.ConvertObjectToDataService;
//import gcp.project.cloud.service.parsing.ClientRequiredToJsonConverterImpl;
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.List;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mockito;
//import org.modelmapper.ModelMapper;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.times;
//
//public class ClientRequiredInfoServiceImplTest {
//    private static final String FILE_CLIENT_REQUIRED =
//            "src/test/resources/test_clients_required_upload.json";
//    private static final String TEST_STRING = "Test";
//    private ConvertObjectToDataService<ClientRequiredInfo> objectToDataService;
//    private List<Client> clientList;
//    private ModelMapper modelMapper;
//    private ObjectMapper objectMapper;
//    private BigQueryRepository bigQueryRepository;
//    private ClientRequiredInfoService clientRequiredInfoService;
//
//    @Before
//    public void configureDependencies() {
//        bigQueryRepository = Mockito.mock(BigQueryRepository.class);
//        objectMapper = new ObjectMapper();
//        objectToDataService = new ClientRequiredToJsonConverterImpl(objectMapper);
//        modelMapper = new ModelMapper();
//        clientRequiredInfoService = new ClientRequiredInfoServiceImpl(objectToDataService,
//                bigQueryRepository, modelMapper);
//        clientList = List.of(new Client(1L, "John", "1234", "address"));
//    }
//
//    @Test
//    public void getClientRequiredDtoTest() {
//        List<ClientRequiredInfo> actual = clientRequiredInfoService.getClientRequiredDto(clientList);
//        List<ClientRequiredInfo> expected = List.of(new ClientRequiredInfo(1L, "John"));
//        Assert.assertEquals(expected, actual);
//    }
//
//    @Test
//    public void uploadClientsRequiredDtoTest() {
//        List<ClientRequiredInfo> clientRequiredInfos = List.of(new ClientRequiredInfo(1L, "John"));
//        clientRequiredInfoService.uploadClientsRequiredInfo(clientRequiredInfos, TEST_STRING,
//                TEST_STRING, FILE_CLIENT_REQUIRED);
//        verify(bigQueryRepository, times(1)).writeToTable(anyString(), anyString(), anyString());
//        String expected = "{\"id\":1,\"name\":\"John\"}";
//        String actual = null;
//        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_CLIENT_REQUIRED))) {
//            actual = bufferedReader.readLine();
//        } catch (IOException e) {
//            Assert.fail("File doesn't exist " + e);
//        }
//        Assert.assertEquals(expected, actual);
//    }
//}
