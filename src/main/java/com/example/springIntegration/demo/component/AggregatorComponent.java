//package com.example.springIntegration.demo.component;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.integration.channel.DirectChannel;
//import org.springframework.integration.dsl.IntegrationFlow;
//import org.springframework.messaging.MessageChannel;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//public class AggregatorComponent {
//
//    @Bean
//    public MessageChannel aggregatorInput() {
//        return new DirectChannel();
//    }
//
//    @Bean
//    public MessageChannel aggregatorOutput() {
//        return new DirectChannel();
//    }
//
//    @Bean
//    public IntegrationFlow aggregatorFlow() {
//        return IntegrationFlow.from(aggregatorInput())
//                .aggregate(a -> a
//                        .correlationStrategy(m -> m.getHeaders().getId()) // Use message ID to correlate
//                        .releaseStrategy(g -> g.size() == 2) // Wait for 2 parts
//                        .outputProcessor(group -> {
//                            Map<String, Object> combined = new HashMap<>();
//                            group.getMessages().forEach(msg -> combined.putAll((Map) msg.getPayload()));
//                            return combined;
//                        }))
//                .channel(aggregatorOutput())
//                .get();
//    }
//}