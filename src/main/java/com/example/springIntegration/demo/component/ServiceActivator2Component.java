package com.example.springIntegration.demo.component;

import com.example.springIntegration.demo.FlowConfig;
import com.example.springIntegration.demo.component.data.ServiceActivatorConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.handler.MethodInvokingMessageProcessor;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Configuration()
public class ServiceActivator2Component {

    private final GenericApplicationContext context;
    private final FlowConfig configProperties;
    private final SpelExpressionParser parser = new SpelExpressionParser();

    public ServiceActivator2Component(GenericApplicationContext context,
                                      FlowConfig configProperties) {
        this.context = context;
        this.configProperties = configProperties;
    }

    @Bean
    public MessageChannel serviceInput2() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel serviceOutput2() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow serviceActivator2(FlowConfig flowConfig) {
        ServiceActivatorConfig config = flowConfig.getServiceActivators().get(1);

        return IntegrationFlow
                .from(serviceInput2())
                .handle((payload, headers) -> {
                    Object bean = context.getBean(config.getBean());
                    Method method = resolveTargetMethod(bean, config);

                    List<Object> args = Optional.ofNullable(config.getArgs())
                            .orElse(List.of())
                            .stream()
                            .map(arg -> parser.parseExpression(arg.getExpression()).getValue(payload))
                            .toList();

                    MethodInvokingMessageProcessor<Object> processor = new MethodInvokingMessageProcessor<>(bean, method);
                    processor.setUseSpelInvoker(false);
                    processor.setBeanFactory(context);

                    Object result = processor.processMessage(MessageBuilder.withPayload(args.toArray()).copyHeaders(headers).build());

                    return MessageBuilder.withPayload(
                                    Map.of("source", "service",
                                            "payload", payload,
                                            "data", result))
                            .copyHeaders(headers)
                            .build();
                })
                .channel(serviceOutput2())
                .get();
    }


    private Method resolveTargetMethod(Object bean, ServiceActivatorConfig config) {
        Method[] methods = bean.getClass().getMethods();
        for (Method m : methods) {
            if (m.getName().equals(config.getMethod()) &&
                    m.getParameterCount() == (config.getArgs() != null ? config.getArgs().size() : 0)) {
                return m;
            }
        }
        throw new IllegalArgumentException("No matching method found for: " + config.getMethod());
    }
}