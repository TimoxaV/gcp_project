package gcp.project.cloud.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import gcp.project.cloud.dto.DataAttributesDto;
import gcp.project.cloud.dto.MessageDto;
import gcp.project.cloud.dto.NotificationDto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Base64;

public class PubSubServiceTest {
    private ObjectMapper objectMapper;
    private PubSubService pubSubService;

    @Before
    public void getObjectMapper() {
        objectMapper = new ObjectMapper();
        pubSubService = new PubSubService(objectMapper);
    }

    @Test
    public void getDataAttributes() {
        DataAttributesDto expected = new DataAttributesDto("test", "test");
        String storageInfo = "{\"name\":\"test\",\"bucket\":\"test\"}";
        MessageDto messageDto = new MessageDto(null,
                Base64.getEncoder().encodeToString(storageInfo.getBytes()), null);
        NotificationDto notificationDto = new NotificationDto(messageDto, "test");
        DataAttributesDto actual = pubSubService.getDataAttributes(notificationDto);
        Assert.assertEquals(expected, actual);
    }
}
