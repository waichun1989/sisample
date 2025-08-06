package com.example.springIntegration.demo.component;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.http.dsl.Http;
import org.springframework.messaging.MessageChannel;

import java.util.Map;

@Configuration
public class HttpOutboundComponent {

    @Bean
    public MessageChannel httpOutboundInput() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow httpOutboundFlow() {
        return IntegrationFlow
                .from(httpOutboundInput())
                .handle(Http.outboundGateway("https://webhook.site/83726c07-683f-478e-bb76-c5303c359c80")
                        .httpMethod(HttpMethod.POST)
                        .expectedResponseType(String.class) // this is for the body type
                        .extractResponseBody(false))
                .handle((payload, headers) -> {
                    // payload here is the response body from webhook.site
                    System.out.println("Webhook response: " + payload);

                    if (payload instanceof ResponseEntity) {
                        ResponseEntity<?> response = (ResponseEntity<?>) payload;

                        // You now have access to:
                        System.out.println("Status Code: " + response.getStatusCode());
                        System.out.println("Headers: " + response.getHeaders());
                        System.out.println("Body: " + response.getBody());

                        return Map.of("source", "http", "data", response);
                    }

                    // Fallback
                    return Map.of("source", "http", "data", payload);
                })
                .get();
    }
}
