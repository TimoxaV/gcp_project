package gcp.project.cloud.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Configuration
public class Exchange {

    @MessagingGateway(defaultRequestChannel = GcpPubSub.OUTPUT_CHANNEL)
    public interface CustomMessageGateway {
        @Gateway(requestChannel = GcpPubSub.OUTPUT_CHANNEL)
        void sendToPubSub(TestPayload payload);
    }

    @ServiceActivator(inputChannel = GcpPubSub.INPUT_CHANNEL)
    public void getFromPubSub(@Payload TestPayload payload) {
        //some logic
        System.out.println(payload);
    }
}
