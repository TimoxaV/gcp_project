package gcp.project.cloud.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import gcp.project.cloud.dto.MessageDto;
import gcp.project.cloud.dto.NotificationDto;
import gcp.project.cloud.exceptions.DataProcessException;
import gcp.project.cloud.service.ClientRequiredInfoService;
import gcp.project.cloud.service.ClientService;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProcessControllerTest {
    private static final String URL_PROCESS = "/process";
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Map<String, String> attributes;
    @Spy
    private ClientService clientService;
    @Spy
    private ClientRequiredInfoService clientRequiredInfoService;
    @InjectMocks
    private ProcessController processController;
    @InjectMocks
    private CustomGlobalExceptionHandler exceptionHandler;

    @Before
    public void configureTestInstances() {
        mockMvc = MockMvcBuilders.standaloneSetup(processController).setControllerAdvice(exceptionHandler).build();
        objectMapper = new ObjectMapper();
        attributes = new HashMap<>();
        attributes.put("bucketId", "test");
        attributes.put("objectId", "test");
    }

    @Test
    public void helloTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello"));
    }

    @Test
    public void processTest() throws Exception {
        NotificationDto notificationDto = new NotificationDto(
                new MessageDto(attributes, "test", "test"), "test");
        String requestBody = objectMapper.writeValueAsString(notificationDto);
        MediaType mediaType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);
        verifyNoMoreInteractions(clientService);
        verifyNoMoreInteractions(clientRequiredInfoService);
        mockMvc.perform(MockMvcRequestBuilders.post(URL_PROCESS)
                .contentType(mediaType)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("Finish processing: test / test"));
        InOrder inOrder = Mockito.inOrder(clientService, clientRequiredInfoService);
        inOrder.verify(clientService, times(1)).downloadClients(any(), any(), any());
        inOrder.verify(clientService, times(1))
                .uploadClients(anyList(), any(), any(), any());
        inOrder.verify(clientRequiredInfoService, times(1)).getClientRequiredDto(anyList());
        inOrder.verify(clientRequiredInfoService, times(1))
                .uploadClientsRequiredInfo(anyList(), any(), any(), any());
    }

    @Test
    public void processTestNullMessage() throws Exception {
        NotificationDto notificationDto = new NotificationDto(null, "test");
        String requestBody = objectMapper.writeValueAsString(notificationDto);
        MediaType mediaType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);
        mockMvc.perform(MockMvcRequestBuilders.post(URL_PROCESS)
                .contentType(mediaType)
                .content(requestBody))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors").isArray());
        verify(clientService, times(0)).downloadClients(anyString(), anyString(), anyString());
        verify(clientRequiredInfoService, times(0)).getClientRequiredDto(anyList());
    }

    @Test
    public void processTestMessageNotReadable() throws Exception {
        String requestBody = "\"message\":\"test\", \"subscription\":\"test\"";
        MediaType mediaType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);
        mockMvc.perform(MockMvcRequestBuilders.post(URL_PROCESS)
                .contentType(mediaType)
                .content(requestBody))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.message", is("Notification's JSON request is not in appropriate form.")));
        verify(clientService, times(0)).downloadClients(anyString(), anyString(), anyString());
        verify(clientRequiredInfoService, times(0)).getClientRequiredDto(anyList());
    }

    @Test
    public void processTestMessageNoFile() throws Exception {
        NotificationDto notificationDto = new NotificationDto(
                new MessageDto(attributes, "test", "test"), "test");
        String requestBody = objectMapper.writeValueAsString(notificationDto);
        MediaType mediaType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);
        when(clientService.downloadClients(anyString(), anyString(), any()))
                .thenThrow(NullPointerException.class);
        mockMvc.perform(MockMvcRequestBuilders.post(URL_PROCESS)
                .contentType(mediaType)
                .content(requestBody))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.status", is("NOT_FOUND")))
                .andExpect(jsonPath("$.message", is("It seems there is no such bucket or object is Storage")));
        verify(clientService, times(1)).downloadClients(any(), any(), any());
        verifyNoMoreInteractions(clientService);
        verifyNoMoreInteractions(clientRequiredInfoService);
    }

    @Test
    public void processTestDataProcessException() throws Exception {
        NotificationDto notificationDto = new NotificationDto(
                new MessageDto(attributes, "test", "test"), "test");
        String requestBody = objectMapper.writeValueAsString(notificationDto);
        MediaType mediaType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);
        when(clientService.downloadClients(anyString(), anyString(), any()))
                .thenThrow(DataProcessException.class);
        mockMvc.perform(MockMvcRequestBuilders.post(URL_PROCESS)
                .contentType(mediaType)
                .content(requestBody))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.status", is("NOT_FOUND")));
        verify(clientService, times(1)).downloadClients(anyString(), anyString(), any());
        verifyNoMoreInteractions(clientRequiredInfoService);
    }
}
