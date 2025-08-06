package com.example.springIntegration.demo;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlowBuilder;
import org.springframework.messaging.MessageChannel;

import java.util.Map;

@Configuration
@EnableIntegration
public class FlowBuilder {

    private final FlowConfig flowConfig;
    private final ConfigurableApplicationContext context;

    public FlowBuilder(FlowConfig flowConfig, ConfigurableApplicationContext context) {
        this.flowConfig = flowConfig;
        this.context = context;
    }

    @Bean
    public IntegrationFlow dynamicFlow() {
        IntegrationFlowBuilder builder = null;

        for (Map<String, String> link : flowConfig.getFlow()) {
            MessageChannel from = context.getBean(link.get("from"), MessageChannel.class);
            MessageChannel to = context.getBean(link.get("to"), MessageChannel.class);

            if (builder == null) {
                builder = IntegrationFlow.from(from).channel(to);
            } else {
                builder = builder.channel(from).channel(to);
            }
        }

        return builder != null ? builder.get() : null;
    }
}
