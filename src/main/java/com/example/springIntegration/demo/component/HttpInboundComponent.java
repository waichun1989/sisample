package com.example.springIntegration.demo.component;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.http.dsl.Http;
import org.springframework.messaging.MessageChannel;

@Configuration
public class HttpInboundComponent {

    @Bean
    public MessageChannel httpInboundChannel() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow httpInboundFlow() {
        return IntegrationFlow
                .from(Http.inboundGateway("/receive")
                        .requestPayloadType(String.class))
                .channel(httpInboundChannel())
                .get();
    }
}
