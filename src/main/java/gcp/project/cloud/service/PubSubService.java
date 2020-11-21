package gcp.project.cloud.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import gcp.project.cloud.dto.DataAttributesDto;
import gcp.project.cloud.dto.MessageDto;
import gcp.project.cloud.dto.NotificationDto;
import gcp.project.cloud.exceptions.NotificationException;
import java.io.IOException;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PubSubService {
    private final ObjectMapper objectMapper;

    @Autowired
    public PubSubService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public DataAttributesDto getDataAttributes(NotificationDto notificationDto) {
        MessageDto message = notificationDto.getMessage();
        byte[] decodedData = Base64.getDecoder().decode(message.getData());
        try {
            return objectMapper.readValue(decodedData, DataAttributesDto.class);
        } catch (IOException e) {
            throw new NotificationException("Can't get data from notification's message", e);
        }
    }
}
