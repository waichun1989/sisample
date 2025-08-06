package com.example.springIntegration.demo.component;

import com.example.springIntegration.demo.FlowConfig;
import com.example.springIntegration.demo.component.data.ServiceActivatorConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.support.MessageBuilder;
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
    public MessageChannel serviceInput1() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel serviceOutput1() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow serviceActivator(FlowConfig flowConfig) {
        ServiceActivatorConfig config = flowConfig.getServiceActivators().get(0);
        return IntegrationFlow
                .from(serviceInput1())
                .handle((payload, headers) -> {
                    Object bean = context.getBean(config.getBean());
                    Method method = Arrays.stream(bean.getClass().getMethods())
                            .filter(m -> m.getName().equals(config.getMethod()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Method not found: " + config.getMethod()));

                    List<Object> args = Optional.ofNullable(config.getArgs())
                            .orElse(List.of())
                            .stream()
                            .map(arg -> parser.parseExpression(arg.getExpression()).getValue(payload))
                            .toList();

                    Object result = ReflectionUtils.invokeMethod(method, bean, args.toArray());

                    return MessageBuilder.withPayload(
                                    Map.of("source", "service",
                                            "payload", payload,
                                            "data", result))
                            .copyHeaders(headers)
                            .build();
                })
                .channel(serviceOutput1())
                .get();
    }
}