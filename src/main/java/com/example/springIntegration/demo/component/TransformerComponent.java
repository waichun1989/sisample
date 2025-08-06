package com.example.springIntegration.demo.component;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.MessageChannel;

@Configuration
public class TransformerComponent {

    @Bean
    public MessageChannel transformerInput() {
        return MessageChannels.direct().getObject();
    }

    @Bean
    public MessageChannel transformerOutput() {
        return MessageChannels.direct().getObject();
    }

    @Bean
    public IntegrationFlow transformerFlow() {
        return IntegrationFlow
                .from(transformerInput())
                .transform(String.class, String::toUpperCase)
                .channel(transformerOutput())
                .get();
    }
}
