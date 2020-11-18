package gcp.project.cloud.controllers;

import gcp.project.cloud.service.ClientRequiredDtoService;
import gcp.project.cloud.service.ClientService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProcessControllerTest {
    private MockMvc mockMvc;
    @Mock
    private ClientService clientService;
    @Mock
    private ClientRequiredDtoService clientRequiredDtoService;
    @InjectMocks
    private ProcessController processController;

    @Before
    public void configureTestInstances() {
        mockMvc = standaloneSetup(processController).build();
    }

    @Test
    public void helloTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello"));
    }

    @Test
    public void processTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/process"))
                .andExpect(status().isOk())
                .andExpect(content().string("All processed"));
    }
}