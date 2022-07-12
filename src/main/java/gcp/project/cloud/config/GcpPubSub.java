package gcp.project.cloud.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.integration.AckMode;
import org.springframework.cloud.gcp.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import org.springframework.cloud.gcp.pubsub.integration.outbound.PubSubMessageHandler;
import org.springframework.cloud.gcp.pubsub.support.converter.JacksonPubSubMessageConverter;
import org.springframework.cloud.gcp.pubsub.support.converter.PubSubMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class GcpPubSub {
    public static final String INPUT_CHANNEL = "inputChannel";
    public static final String OUTPUT_CHANNEL = "outputChannel";

    @Bean(INPUT_CHANNEL)
//    @Qualifier(INPUT_CHANNEL)
    public MessageChannel messageInputChannel() {
        return new DirectChannel();
    }

    @Bean(OUTPUT_CHANNEL)
//    @Qualifier(OUTPUT_CHANNEL)
    public MessageChannel messageOutputChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = OUTPUT_CHANNEL)
    public MessageHandler messageHandler(PubSubTemplate template) {
        return new PubSubMessageHandler(template, "test-topic");
    }

    @Bean
    public PubSubInboundChannelAdapter channelAdapter(
            @Qualifier(INPUT_CHANNEL) MessageChannel messageChannel, PubSubTemplate template) {
        PubSubInboundChannelAdapter channelAdapter = new PubSubInboundChannelAdapter(template, "test-subscription");
        channelAdapter.setAckMode(AckMode.AUTO);
        channelAdapter.setOutputChannel(messageChannel);
        channelAdapter.setPayloadType(TestPayload.class);
        return channelAdapter;
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public PubSubMessageConverter pubSubMessageConverter(ObjectMapper objectMapper) {
        return new JacksonPubSubMessageConverter(objectMapper);
    }
}
