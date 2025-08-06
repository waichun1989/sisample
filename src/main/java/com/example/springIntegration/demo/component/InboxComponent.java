package com.example.springIntegration.demo.component;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.MessageChannel;

@Configuration
public class InboxComponent {

    @Bean
    public MessageChannel inboxInput() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel inboxOutput() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow inboxFlow() {
        return IntegrationFlow
                .from(inboxInput())
                .handle((payload, headers) -> {
                    System.out.println("Processing inbox");
                    return payload;
                })
                .channel(inboxOutput())
                .get();
    }
}
