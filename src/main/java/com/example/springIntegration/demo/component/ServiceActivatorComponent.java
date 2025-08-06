package com.example.springIntegration.demo.component;

import com.example.springIntegration.demo.FlowConfig;
import com.example.springIntegration.demo.component.data.ServiceActivatorConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.integration.core.GenericHandler;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Configuration
public class ServiceActivatorComponent {

    private final GenericApplicationContext context;
    private final FlowConfig configProperties;
    private final SpelExpressionParser parser = new SpelExpressionParser();

    public ServiceActivatorComponent(GenericApplicationContext context,
                                     FlowConfig configProperties) {
        this.context = context;
        this.configProperties = configProperties;
    }

    @Bean
    public List<IntegrationFlow> dynamicServiceActivators() {
        return configProperties.getServiceActivators().stream()
                .map(this::buildFlow)
                .toList();
    }

    private IntegrationFlow buildFlow(ServiceActivatorConfig config) {
        return IntegrationFlow
                .from(context.getBean(config.getInput(), MessageChannel.class))
                .handle((GenericHandler<Message<?>>) (message, headers) -> {
                    Object bean = context.getBean(config.getBean());
                    Method method = Arrays.stream(bean.getClass().getMethods())
                            .filter(m -> m.getName().equals(config.getMethod()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Method not found: " + config.getMethod()));

                    List<Object> args = Optional.ofNullable(config.getArgs())
                            .orElse(List.of())
                            .stream()
                            .map(arg -> parser.parseExpression(arg.getExpression()).getValue(new RootObject(message)))
                            .toList();

                    Object result = ReflectionUtils.invokeMethod(method, bean, args.toArray());

                    return Map.of("source", "service", "data", result);
                })
                .channel(context.getBean(config.getOutput(), MessageChannel.class))
                .get();
    }

    static class RootObject {
        private final Message<?> message;

        public RootObject(Message<?> message) {
            this.message = message;
        }

        public Object getPayload() {
            return message.getPayload();
        }

        public Map<String, Object> getHeaders() {
            return message.getHeaders();
        }

        public Message<?> getMessage() {
            return message;
        }
    }
}