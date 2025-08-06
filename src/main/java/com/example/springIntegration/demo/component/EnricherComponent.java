package com.example.springIntegration.demo.component;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.MessageChannel;

@Configuration
public class EnricherComponent {

    @Bean
    public MessageChannel enricherInput() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel enricherOutput() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow enricherFlow() {
        return IntegrationFlow
                .from(enricherInput())
                .enrich(e -> e.property("headers.enriched", "true"))
                .channel(enricherOutput())
                .get();
    }
}
